package server;

import utils.FileHandler;
import utils.InputValidation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    public static volatile FileHandler file;

    public static volatile boolean GAME_END = false;
    public static volatile String WINNER = "";

    public static String WORD;
    private static int MAX_PLAYERS = 10;
    private static int TIMEOUT_WAIT_PLAYERS = 15000; //ms
    private static int MAX_GAME_TIME = 60;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Server: Jogo da forca");
        file = new FileHandler(new File("src/server/resources/users.txt").getAbsolutePath());

        int portNumber = InputValidation.validateIntBetween(
                sc,
                "Digite a porta que deseja ouvir (1024...65535): ",
                1024, 65535);

        do {
            System.out.println("Digite a palavra a ser descoberta (Exceto a palavra 'desisto'):");
            WORD = sc.nextLine();
        } while (WORD.equalsIgnoreCase("desisto"));

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                ExecutorService executorService = Executors.newFixedThreadPool(MAX_PLAYERS)
                ) {

            //Tempo limite para aguardar a entrada de novos jogadores
            serverSocket.setSoTimeout(TIMEOUT_WAIT_PLAYERS);

            while (true) {
                System.out.println("Main: Esperando a chegada de novos jogadores.");
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Main: Nova liga��o");
                    executorService.execute(new ServerThread(clientSocket));
                } catch (SocketTimeoutException e) {
                    System.out.println("Main: Acabou o tempo para aceitar novos jogadores");
                    executorService.shutdownNow();
                    break;
                }
            }

            //Limpar usu�rios logados
            file.resetFile();

            if (!executorService.awaitTermination(MAX_GAME_TIME, TimeUnit.SECONDS)) {
                //Timeout terminou
                GAME_END = true;
                System.out.println("Main: O tempo de jogo terminou.");
            }

        } catch (IOException e) {
            System.err.println("Main: Ocorreu um erro de I/O ao iniciar o socket");
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("Main: Ocorreu um erro em awaitTermination");
            System.exit(3);
        }


        sc.close();

    }

}
