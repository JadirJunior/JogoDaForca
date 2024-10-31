package client;

import utils.InputValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {


    static int attempts = 3;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o hostname do servidor: ");
        String hostname = sc.nextLine();

        int portNumber = InputValidation.validateIntBetween(sc, "Digite a porta que irá se conectar (1024...65535): ", 1024, 65535);

        try (
                Socket clientSocket = new Socket(hostname, portNumber);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            boolean logged = false;

            do {
                // Enviando as credenciais para login
                System.out.print("Username: ");
                out.println(sc.nextLine());
                System.out.print("Password: ");
                out.println(sc.nextLine());

                // Recuperando os status
                String status = in.readLine();

                // Tratando os casos
                switch (status.toLowerCase()) {
                    // Okay
                    case "ok":
                        System.out.println("Logado com sucesso.");
                        logged = true;
                        break;
                    // Jogo já iniciou
                    case "game_start":
                        System.out.println("O tempo para autenticação finalizou, o jogo já iniciou.");
                        System.exit(0);
                    // Credenciais inválidas
                    default:
                        attempts--;
                        System.out.println(status);
                        if (attempts == 0) {
                            System.out.println("Acabaram suas tentativas.");
                            System.exit(0);
                        } else {
                            System.out.println("Tentativas restantes: " + attempts);
                        }
                }

            } while(!logged);

            System.out.println("Aguarde o jogo começar...");
            System.out.println(in.readLine());
            System.out.println("Em qualquer momento pode digitar 'Desisto' para sair do jogo");

            // Jogo comecou

            String guess = "";
            do {
                String actualString = in.readLine();
                System.out.println(actualString);

                guess = sc.nextLine();
                //Enviar palpite
                out.println(guess);

                String feedback = in.readLine();

                switch (feedback) {
                    case "winner":
                        System.out.println("Parabéns! Você venceu o jogo!");
                        return;
                    case "correct":
                        System.out.println("Acertou na letra");
                        break;
                    case "incorrect":
                        System.out.println("Errou na letra");
                        break;
                    case "desistencia":
                        System.out.println("Que pena que você desistiu... \nObrigado por jogar.");
                        return;
                    default:
                        System.out.println(feedback);
                        return;
                }

            } while (!guess.equalsIgnoreCase("desisto"));

        } catch (UnknownHostException e) {
            System.err.println("Host não encontrada");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro de I/O ao iniciar o socket");
            System.exit(1);
        }

    }
}
