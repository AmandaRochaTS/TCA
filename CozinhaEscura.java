import java.util.Random;
import java.util.Scanner;

public class CozinhaEscura {
    public final static Scanner TECLADO = new Scanner(System.in);
    public final static Random OBJETOS = new Random();

    public static int x = 10;
    public static int y = 20;
    public static int tempoLimite = 0;
    public static int ingredTotal = 0;
    public final static int LARGURA = 50;
    public final static int ALTURA = 25;

    public static void imprimir(String texto) {
        System.out.println(texto);
    }

    public static int lerValorInteiro() {
        int valor = TECLADO.nextInt();
        return valor;
    }

    public static void preencherCozinha(char cozinha[][]) {

        for (int i = 0; i < ALTURA; i++) {
            for (int j = 0; j < LARGURA; j++) {
                cozinha[i][j] = '.';

            }
        }
    }

    public static void imprimirCozinha(char cozinha[][]) {
        for (int i = 0; i < ALTURA; i++) {
            for (int j = 0; j < LARGURA; j++) {
                if (i == y && j == x) {
                    System.out.print('H');
                } else {
                    System.out.print(cozinha[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println("Os ingredientes são '*'\nOs ratos estão invisíveis...");
    }

    public static void colocarIngredientes(char[][] cozinha, int ingredTotal) {

        for (int i = 0; i < ingredTotal; i++) {
            int ingredientes1Larg = OBJETOS.nextInt(50);
            int ingredientes1Alt = OBJETOS.nextInt(25);
            if (cozinha[ingredientes1Alt][ingredientes1Larg] == '.') {
                cozinha[ingredientes1Alt][ingredientes1Larg] = '*';
            } else {
                i--;
            }
        }
    }

    public static void colocarRatos(char[][] cozinha, char[][] cozinha2, int ratosTotal) {

        for (int j = 0; j < ratosTotal;) {
            int ratosLarg = OBJETOS.nextInt(50);
            int ratosAlt = OBJETOS.nextInt(25);
            if (cozinha[ratosAlt][ratosLarg] == '.') {
                cozinha2[ratosAlt][ratosLarg] = '^';
                j++;
            } else if (cozinha[ratosAlt][ratosLarg] == '*') {
                if (ratosAlt - 2 >= 0 && ratosLarg - 1 >= 0 && cozinha[ratosAlt - 2][ratosLarg - 1] == '.') {
                    cozinha2[ratosAlt - 2][ratosLarg - 1] = '^';
                    j++;
                } else if(cozinha[ratosAlt + 2][ratosLarg + 1] == '.'){
                    cozinha2[ratosAlt + 2][ratosLarg + 1] = '^';
                    j++;
                }
            }
        }

    }

    public static void colocarBuzina(char[][] cozinha, int buzina) {

        int buzinaAlt = 0;
        int buzinaLarg = 0;

        do {
            buzinaLarg = OBJETOS.nextInt(50);
            buzinaAlt = OBJETOS.nextInt(25);
        } while (cozinha[buzinaAlt][buzinaLarg] != '.');
        cozinha[buzinaAlt][buzinaLarg] = '&';

    }

    public static void pegouNaoPegou(int escolha, int tempoRato, int ingredientes, char cozinha[][], char cozinha2[][],
            int novoIngredAlt, int novoIngredLarg, boolean buzina, double tempoAgora) {

        if (escolha == 1) {
            tempoRato = 3;
            tempoAgora = BibliotecaTCA.segundosPassados();
            imprimir("Aperte 1 pra pegar de volta!");
            BibliotecaTCA.esperarTecla();
            if (BibliotecaTCA.segundosPassados() - tempoAgora >= tempoRato) {
                ingredientes--;
                cozinha2[y][x] = '.';
                novoIngredLarg = OBJETOS.nextInt(50);
                novoIngredAlt = OBJETOS.nextInt(25);
                if (cozinha[novoIngredAlt][novoIngredLarg] == '.')
                    cozinha[novoIngredAlt][novoIngredLarg] = '*';
            } else {
                imprimir("Você pegou o ingrediente de volta!");
            }

        } else if (escolha == 2 && buzina == true) {
            BibliotecaTCA.tocarSom("Sons/226 (2).wav", true);
            // Som de buzina: https://mixkit.co/free-sound-effects/horn/
            cozinha2[y][x] = '.';
            imprimir("Você espantou o rato!");
            if (y + 3 < ALTURA && x + 3 < LARGURA) {
                cozinha2[y + 3][x + 3] = '^';
            } else if (y - 3 >= 0 && x - 3 >= 0) {
                cozinha2[y - 3][x - 3] = '^';
            }
        }
    }

    public static void main(String[] args) {

        String nomeJogador;
        int ingredientes = 0;
        char cozinha[][];
        char cozinha2[][];
        cozinha = new char[25][50];
        cozinha2 = new char[25][50];
        boolean buzina = false;
        int novoIngredAlt = 0;
        int novoIngredLarg = 0;
        int escolha = 0;
        double tempoAgora = 0;

        imprimir(
                "[0;37m [0;35m▄▓▓▓▓▓[0;37m   [0;35m▄▓▓▓▄[0;37m   [0;35m▓▓▓▓▓▄[0;37m   [0;35m▓▓▓[0;37m  [0;35m▓▓▓▓▓▄[0;37m   [0;35m▓▓▓[0;37m  [0;35m▓▓[0;37m   [0;35m▄▓▓▓▄[0;37m         [0;35m▄▓▓▓▓▓[0;37m   [0;35m▄▓▓▓▓▓[0;37m   [0;35m▄▓▓▓▓▓[0;37m  [0;35m▓▓▓[0;37m  [0;35m▓▓[0;37m  [0;35m▓▓▓▓▓▄[0;37m    [0;35m▄▓▓▓▄[0;37m  [0m\n"
                        + //
                        "[0;35;45m [0;35m██▀[0;37m     [0;35m███[0;37m  [0;35m██[0;37m      [0;35m▀██[0;37m  [0;35m███[0;37m  [0;35m███[0;37m [0;35m▀██[0;37m  [0;35m███[0;37m  [0;35m██[0;37m  [0;35m███[0;37m  [0;35m██[0;37m       [0;35m███▀[0;37m     [0;35m███[0;37m      [0;35;45m [0;35m██▀[0;37m     [0;35m███[0;37m  [0;35m██[0;37m  [0;35m███[0;37m [0;35m▀██[0;37m  [0;35m███[0;37m  [0;35m██[0;37m [0m\n"
                        + //
                        "[0;96;45m░░░[0;35m▌[0;37m     [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0;37m      [0;96;45m░░[0;35m▌[0;37m [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0;37m [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0;37m [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0;37m      [0;96;45m░░░[0;35m▌[0;37m     [0;96;45m░░░[0;35m▌[0;37m     [0;96;45m░░░[0;35m▌[0;37m     [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0;37m [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0;37m [0;96;45m░░░[0;35m▌[0;37m [0;96;45m░░[0;35m▌[0m\n"
                        + //
                        "[0;96;45m▒▒▒[0;35m▌[0;37m     [0;96;45m▒▒▒[0;35m▌[0;37m [0;96;45m▒▒[0;35m▌[0;37m [0;95m▄[0;96;45m▒▒▒▒▒[0;95m▀[0;37m  [0;96;45m▒▒▒[0;35m▌[0;37m [0;96;45m▒▒▒[0;35m▌[0;37m [0;96;45m▒▒[0;35m▌[0;37m [0;96;45m▒▒▒▒▒▒▒[0;35m▌[0;37m [0;96;45m▒▒▒▒▒▒▒[0;35m▌[0;37m      [0;96;45m▒▒▒▒▒[0;35m▌[0;37m   [0;95m▀[0;96;45m▒▒▒▒▒[0;95m▄[0;37m  [0;96;45m▒▒▒[0;35m▌[0;37m     [0;96;45m▒▒▒[0;35m▌[0;37m [0;96;45m▒▒[0;35m▌[0;37m [0;35m▀▀▀[0;37m [0;96m▄[0;96;45m▒▒[0;35m▌[0;37m [0;96;45m▒▒▒▒▒▒▒[0;35m▌[0m\n"
                        + //
                        "[0;96m███[0;96;45m▄[0;35m▄[0;37m    [0;96m███[0;35m▌[0;37m [0;96m██[0;35m▌[0;37m [0;96m███[0;35m▌[0;37m     [0;96m███[0;35m▌[0;37m [0;96m███[0;35m▌[0;37m [0;96m██[0;35m▌[0;37m [0;96m███[0;35m▌[0;37m [0;96m██[0;35m▌[0;37m [0;96m███[0;35m▌[0;37m [0;96m██[0;35m▌[0;37m      [0;96m███[0;37m           [0;96m██[0;35m▌[0;37m [0;96m███[0;96;45m▄[0;35m▄[0;37m    [0;96m███[0;35m▌[0;37m [0;96m██[0;35m▌[0;37m [0;96m██████[0;35m█[0;37m  [0;96m███[0;35m▌[0;37m [0;96m██[0;35m▌[0m\n"
                        + //
                        "[0;37m [0;97m▀█████[0;35m▌[0;37m  [0;97m▀███[0;97;45m▀[0;35m▀[0;37m  [0;97m▀██████[0;35m▌[0;37m [0;97m███[0;35m▌[0;37m [0;97m███[0;35m▌[0;37m [0;97m██[0;35m▌[0;37m [0;97m███[0;35m▌[0;37m [0;97m██[0;35m▌[0;37m [0;97m███[0;35m▌[0;37m [0;97m██[0;35m▌[0;37m      [0;97m███████[0;35m▌[0;37m [0;97m██████▀[0;37m   [0;97m▀█████[0;35m▌[0;37m [0;97m▀█████[0;97;45m▀[0;37m  [0;97m███[0;35m▌[0;37m [0;97m██[0;35m▌[0;37m [0;97m███[0;35m▌[0;37m [0;97m██[0;35m▌[0m");
        BibliotecaTCA.esperar(3000);
        imprimir("Digite seu nome");
        nomeJogador = TECLADO.nextLine();
        System.out.printf("Bem vindo(a) ao nosso TCA %s!", nomeJogador);
        int lanche = 0;
        System.out.println();
        imprimir(
                "[0;97;41m▄[0;31;41m▒[0;31m▀▀▀[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀▀[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀▄[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀[0;91m░[0;37m [0;97;41m▄[0;31;41m▒[0;91m   [0;37m [0;97;41m▄[0;31;41m▒[0;91m  [0;31m█[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀[0;91m░[0;37m      [0;97;41m▄[0;31;41m▒[0;31m▀▀▀[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀▀[0;37m [0;97;41m▄[0;31;41m▒[0;91m  ░[0;37m      [0;97;41m▄[0;31;41m▒[0;91m   [0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀[0;91m░[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀[0;91m░ [0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀▄[0;37m [0;97;41m▄[0;31;41m▒[0;91m  [0;31m█[0;37m [0;97;41m▄[0;31;41m▒[0;31m▀▀▀[0m\n"
                        + //
                        "[0;91m▀[0;31m▀▀▀[0;91m [0;37m [0;91m▀[0;31m▀▀▀▀[0;37m [0;91m▀[0;31m▀[0;91m   [0;37m [0;91m▀[0;31m▀[0;91m  [0;31m▀[0;37m [0;91m▀[0;31m▀[0;91m   [0;37m [0;91m▀[0;31m▀▀▀▀[0;37m [0;91m▀[0;31m▀[0;91m  [0;31m▀[0;37m      [0;91m▀[0;31m▀▀▀▀[0;37m [0;91m▀[0;31m▀▀▀[0;91m [0;37m [0;91m▀[0;31m▀[0;91m  [0;31m▀[0;37m      [0;91m▀[0;31m▀[0;91m   [0;37m [0;91m▀[0;31m▀[0;91m  [0;31m▀[0;37m [0;91m▀[0;31m▀[0;91m  [0;31m▀[0;91m [0;37m [0;91m▀[0;31m▀[0;91m   [0;37m [0;91m▀[0;31m▀▀▀▀[0;37m [0;91m▀[0;31m▀▀▀[0;91m [0m\n"
                        + //
                        "[0;97;41m░▒▓[0;91m [0;97m▄[0;37m [0;97;41m░▒▓[0;91m [0;97;41m▄[0;37m [0;97;41m░▒▓[0;91m [0;97m▄[0;37m [0;97;41m░▒▓[0;91m [0;97;41m▄[0;37m [0;97;41m░▒▓[0;91m [0;97m▄[0;37m [0;97;41m░▒▓[0;91m [0;97;41m▄[0;37m [0;97;41m░▒▓[0;97m▀█[0;37m      [0;97;41m░▒▓[0;91m [0;97;41m▄[0;37m [0;97;41m░▒▓[0;91m [0;97m▄[0;37m [0;97;41m░▒▓[0;91m [0;97;41m▄[0;37m      [0;97;41m░▒▓[0;91m [0;97m▄[0;37m [0;97;41m░▒▓[0;97m▀█[0;37m [0;97;41m░▒▓[0;91m [0;97;41m▄[0;91m [0;37m [0;97;41m░▒▓[0;91m [0;97m▄[0;37m [0;97;41m░▒▓[0;91m [0;97;41m▄[0;37m [0;97;41m░▒▓[0;91m [0;97m▄[0m\n"
                        + //
                        "[0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀▀[0;91m [0;37m [0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀[0;91m [0;97m▀[0;37m [0;91m▀[0;97m▀▀[0;91m [0;97m▀[0;37m      [0;91m▀[0;97m▀▀▀[0;91m [0;37m [0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀▀▀[0;37m      [0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀[0;91m [0;97m▀[0;37m [0;91m▀[0;97m▀▀[0;91m [0;97m▀▀[0;37m [0;91m▀[0;97m▀▀▀▀[0;37m [0;91m▀[0;97m▀▀[0;91m [0;97m▀[0;37m [0;91m▀[0;97m▀▀▀▀[0m"); // mudar
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // a
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // cor
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // e
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // fonte

        // site: https://coddy.tech/tools/pt/ascii-art-generator
        // site: https://www.asciiart.eu/image-to-ascii
        System.out.println();
        imprimir("""
                           :::::::::.                            :.*-+==#.            .:--==:::..
                    ::...:..:...:::::-:::::::              .-:+=-*+==-**:-        .--::-:.==+=-:-*::
                  ::....:.::::::::-:::::::=::            :-+*+-===-*+==---.     .:....:.:::-===--==-=
                   ----===:..::..:::--:::-*          :--+=+.==+:=%+-=+----.     ......:::::----==-=-::.
                 ..==+*--++=+***+---+++**+.=:     .:=-+=-*=--#*---.::::--:      =--=+*=***##*%#+**+**=-
                .:  . ..:.:-+**###*+++=.    -    .-+*-:=*%-==*:..:::-===      =::=+=#%#*-=++###+##+**+++:
                 :   -=-**++----++=*==:-::-:   .:.-=*+==**+=+=::::-===        =+=+-..:+*###%###%%##=-*+-
                   .:=+**+=+==*==+====--:.     .:::=*#*#*-::::--===:           -=-.:--=+*****+=+***++++
                         ...::::--:.          .=*%#*%=*+=+++==+=.             ::==+=-:::.*##*++*****+-+:
                                                .+**=++**+++++.               -=-====#=+#%#=-*+=*#%***==:
                                                 .-=++++=                     .::-=.:-++-:#+*++++==---=
                                                                                ::--::-==-++****++*+++.
                                                                                  ..:=+++++***##+:..
                                                                                                            """);

        System.out.println();
        imprimir("""
                  .#@@@:                    #%@@@@@#                         *@@@@+
                :@@@@@@:                  @@@    @@@#                       +@=  **+
                %#= @@@:                        %@@@.                             @*
                    @@@:                       @@@@+                         @@%@@+
                    @@@:                     #@@@%.                              .@@
                    @@@:                  :@@@@@@@@@@@*                     *@*  *@*
                                                                             *%%%@+""");

        lanche = lerValorInteiro();
        // Lógica do jogo (desenhar a tela)
        BibliotecaTCA.limparTela();
        preencherCozinha(cozinha);
        preencherCozinha(cozinha2);
        // Controla a velocidade do jogo
        BibliotecaTCA.esperar(100); // espera 100 milissegundos -> 0,1 segundo

        switch (lanche) {
            case 1:
                ingredTotal = 4;
                tempoLimite = 40;
                colocarIngredientes(cozinha, ingredTotal);
                colocarRatos(cozinha, cozinha2, 65);
                imprimir("Você tem 40 segundos para pegar 4 ingredientes!");
                BibliotecaTCA.iniciarCronometro(); // cronômetro começa a contar

                break;
            case 2:
                ingredTotal = 6;
                tempoLimite = 50;
                colocarIngredientes(cozinha, ingredTotal);
                colocarRatos(cozinha, cozinha2, 75);
                colocarBuzina(cozinha, 1);
                imprimir("Você tem 50 segundos para pegar 6 ingredientes!");
                BibliotecaTCA.iniciarCronometro();

                break;
            case 3:
                ingredTotal = 9;
                tempoLimite = 60;
                colocarIngredientes(cozinha, ingredTotal);
                colocarRatos(cozinha, cozinha2, 100);
                colocarBuzina(cozinha, 1);
                imprimir("Você tem 1 minuto para pegar 9 ingredientes!");
                BibliotecaTCA.iniciarCronometro();

                break;
            default:
                imprimir("Inválido!");
                break;
        }

        BibliotecaTCA.esperar(1000); // espera 1 segundo pra começar o jogo
        BibliotecaTCA.limparTela();
        System.out.println("Mova o personagem H com W, A, S, D. Aperte alguma tecla para iniciar e ESC para sair");

        while (true) {

            if (BibliotecaTCA.segundosPassados() >= tempoLimite) { // se o tempo acabar
                BibliotecaTCA.limparTela();
                imprimir(
                        """
                                    [0;36m▄[0;96m█[0;36m▀  [0;37m [0;96m██[0;36m▀[0;96;46m▄[0;36m▄[0;37m [0;36m▄[0;96;46m█[0;96m▀[0;96;46m█▄[0;96m▀[0;96;46m█▄[0;37m [0;36m▄[0;96m█[0;36m▀[0;37m      [0;36m▄[0;96;46m▄[0;36m▀[0;96;46m▄[0;36m▄[0;37m [0;36m▄[0;96;46m▄[0;36m [0;96;46m▄[0;36m▄[0;37m [0;36m▄[0;96m█[0;36m▀[0;37m [0;96m██[0;36m▀[0;96;46m▄[0;36m▄[0m
                                    [0;96m██[0;36m   [0;37m [0;96m██[0;36m [0;96m██[0;37m [0;96;46m██[0;36m [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96m██[0;36m [0;37m      [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96m██[0;36m [0;37m [0;96m██[0;36m [0;96;46m█▀[0m
                                    [0;96m██[0;36m▀[0;96m█[0;96;46m▄[0;37m [0;96m██[0;36m▀[0;96;46m█[0;96m█[0;37m [0;96;46m██[0;36m [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96m██[0;36m▀[0;37m      [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96m██[0;36m▀[0;37m [0;96m██[0;36m▀[0;96;46m█[0;36m▄[0m
                                    [0;96m██[0;36m [0;96m██[0;37m [0;96m██[0;36m [0;96;46m██[0;37m [0;96;46m██[0;36m [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96m██[0;36m [0;37m      [0;96;46m██[0;36m [0;96;46m██[0;37m [0;96;46m▀█[0;36m [0;96;46m█▀[0;37m [0;96m██[0;36m [0;37m [0;96m██[0;36m [0;96;46m██[0m
                                    [0;36m▀[0;96m█[0;36m▄[0;96m██[0;37m [0;96m██[0;36m [0;96;46m██[0;37m [0;96;46m██[0;36m [0;96;46m▀[0;36m▀ [0;96;46m██[0;37m [0;36m▀[0;96m█[0;36m▄[0;37m      [0;36m▀[0;96;46m▀[0;36m▄[0;96;46m▀[0;36m▀[0;37m [0;36m ▀▄▀ [0;37m [0;36m▀[0;96m█[0;36m▄[0;37m [0;96m██[0;36m [0;96;46m██[0m
                                """);
                BibliotecaTCA.tocarSom("Sons/226 (3).wav", true);
                // Som de perda: https://mixkit.co/free-sound-effects/discover/loose/
                BibliotecaTCA.esperarTecla();
                return;
            }

            // Lógica para detectar o movimento
            if (BibliotecaTCA.pressionouTecla()) {
                int tecla = BibliotecaTCA.obtemTeclaPressionada();
                switch (tecla) {
                    case 'a':
                    case 'A':
                        x--;
                        break;
                    case 'd':
                    case 'D':
                        x++;
                        break;
                    case 'w':
                    case 'W':
                        y--;
                        break;
                    case 's':
                    case 'S':
                        y++;
                        break;
                    case 27: // Código ASCII para a tecla ESC
                        imprimir("-Jogo encerrado-");
                        return; // Sai do loop e termina o programa
                }

                // Não deixa o personagem sair da tela
                if (x < 0)
                    x = 0;
                if (x > LARGURA - 1)
                    x = LARGURA - 1;
                if (y < 0)
                    y = 0;
                if (y > ALTURA - 1)
                    y = ALTURA - 1;

                // imprimir a cozinha com o novo local do personagem.
                BibliotecaTCA.limparTela();
                imprimirCozinha(cozinha); // imprime a nova posição do personagem
                if (cozinha[y][x] == '&') { // se a posição atual do personagem tem a buzina
                    buzina = true; // buzina vira true porque o personagem a pegou
                    cozinha[y][x] = '.'; // onde ela estava, vira mapa normal
                } else if (cozinha[y][x] == '*') {
                    ingredientes++;
                    System.out.printf("%d/%d ingredientes", ingredientes, ingredTotal);
                    cozinha[y][x] = '.'; // o ingrediente some e volta a ser mapa normal
                    if (ingredientes == ingredTotal) {
                        BibliotecaTCA.limparTela();
                        imprimir("""
                                 ___________
                                '._==_==_=_.'
                                .-\\:      /-.
                                |            |
                                |(|:.     |) |
                                '-|:.     |-'         VOCÊ GANHOU!
                                  \\::.    /
                                   '::. .'
                                     ) (
                                   _.' '._
                                  `\"\"\"\"\"\"\"`
                                  """);
                        BibliotecaTCA.tocarSom("Sons/226.wav", true);
                        // Som de vitória: https://mixkit.co/free-sound-effects/win/
                        return;
                    }
                } else if (cozinha2[y][x] == '^' && ingredientes > 0) {
                    imprimir("O rato pegou seu ingrediente!\n" +
                            "1- Apertar uma tecla para pegar de volta ou\n" +
                            "2- Use a buzina '&'(se tiver)");
                    while (!BibliotecaTCA.pressionouTecla()) {
                        // n faz nada
                    }
                    char t;
                    while (true) {
                        if (BibliotecaTCA.pressionouTecla()) {
                            int teclaaaaa = BibliotecaTCA.obtemTeclaPressionada();
                            t = (char) teclaaaaa;
                            if (t == '1' || t == '2') {
                                break;
                            }

                        }
                    }
                    escolha = Integer.parseInt("" + (char) t);
                    pegouNaoPegou(escolha, tecla, ingredientes, cozinha, cozinha2, novoIngredAlt, novoIngredLarg,
                            buzina, tempoAgora);
                }
            }
        }
    }
}