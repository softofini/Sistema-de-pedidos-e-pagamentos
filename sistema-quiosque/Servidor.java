import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor{

    private static final int PORTA = 5000;

    // Cardápio fixo
    private static final Map<Integer, Item> cardapio = new HashMap<>();

    static {
        cardapio.put(1, new Item("Batata frita", 8.00));
        cardapio.put(2, new Item("Refrigerante", 5.00));
        cardapio.put(3, new Item("Hambúrguer", 15.00));
        cardapio.put(4, new Item("Sorvete", 6.00));
    }

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor de Pedidos iniciado na porta " + PORTA);

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("Quiosque conectado: " + cliente.getInetAddress());
                pool.execute(new ClienteHandler(cliente));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClienteHandler implements Runnable {

        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter saida = new PrintWriter(
                        socket.getOutputStream(), true)
            ) {
                while (true) {
                    /* Envia cardápio */
                    saida.println("CARDAPIO");
                    for (Map.Entry<Integer, Item> entry : cardapio.entrySet()) {
                        saida.println(entry.getKey() + " - " +
                                entry.getValue().nome + " (R$ " +
                                entry.getValue().preco + ")");
                    }
                    saida.println("FIM_CARDAPIO");

                    /* Recebe pedido (ex: 1,2,3) */
                    String pedidoStr = entrada.readLine();
                    String[] codigos = pedidoStr.split(",");

                    double total = 0.0;
                    List<String> itensPedido = new ArrayList<>();

                    for (String codigo : codigos) {
                        int cod = Integer.parseInt(codigo.trim());
                        Item item = cardapio.get(cod);

                        if (item != null) {
                            itensPedido.add(item.nome + " (R$ " + item.preco + ")");
                            total += item.preco;
                        }
                    }

                    /* Envia resumo do pedido */
                    saida.println("RESUMO_PEDIDO");
                    saida.println("Resumo do pedido:");
                    for (String item : itensPedido) {
                        saida.println("- " + item);
                    }
                    saida.println("Total: R$ " + total);
                    saida.println("FIM_RESUMO");

                    /* Solicita confirmação */
                    saida.println("Confirma pagamento? (SIM/NÃO)");

                    String confirmacao = entrada.readLine();

                    /* Processa pagamento */
                    if ("SIM".equalsIgnoreCase(confirmacao)) {
                        Thread.sleep(2000); // simulação
                        saida.println("Pagamento aprovado! Pedido liberado.");
                        System.out.println("Pedido finalizado com sucesso.");
                    } else {
                        saida.println("Pagamento não confirmado. Pedido cancelado.");
                        System.out.println("Pedido cancelado pelo cliente.");
                    }

                    /* Pergunta se deseja continuar */
                    saida.println("Deseja fazer outro pedido? (SIM/NÃO)");
                    String continuar = entrada.readLine();
                    if (!"SIM".equalsIgnoreCase(continuar)) {
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Cliente desconectado: " + socket.getInetAddress());
        }
    }
}

/* Classe Item */
class Item {
    String nome;
    double preco;

    Item(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }
}
