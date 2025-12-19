import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AtendenteQuiosque {

    private static final String SERVIDOR = "localhost";
    private static final int PORTA = 5000;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(SERVIDOR, PORTA);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(
                    socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                /* Recebe cardápio */
                String linha;
                System.out.println("=== CARDÁPIO ===");

                while (!(linha = entrada.readLine()).equals("FIM_CARDAPIO")) {
                    if (!linha.equals("CARDAPIO")) {
                        System.out.println(linha);
                    }
                }

                /* Envia pedido */
                System.out.print("\nDigite os códigos dos itens (ex: 1,2,3): ");
                String pedido = scanner.nextLine();
                saida.println(pedido);

                /* Recebe resumo do pedido */
                System.out.println("\n=== RESUMO DO PEDIDO ===");
                while (!(linha = entrada.readLine()).equals("FIM_RESUMO")) {
                    if (!linha.equals("RESUMO_PEDIDO")) {
                        System.out.println(linha);
                    }
                }

                /* Confirma pagamento */
                System.out.println(entrada.readLine());
                System.out.print("Resposta: ");
                String confirmacao = scanner.nextLine();
                saida.println(confirmacao);

                /* Resposta final */
                String respostaFinal = entrada.readLine();
                System.out.println("\nServidor: " + respostaFinal);

                /* Pergunta se deseja continuar */
                System.out.print("Deseja fazer outro pedido? (SIM/NÃO): ");
                String continuar = scanner.nextLine();
                saida.println(continuar);

                if (!"SIM".equalsIgnoreCase(continuar)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
