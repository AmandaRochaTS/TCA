import java.util.*;
import com.sun.jna.Native;
import com.sun.jna.Library;

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
        System.out.println("Use A, D, W, S para mover o personagem");
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

        for (int j = 0; j < ratosTotal; j++) {
            int ratosLarg = OBJETOS.nextInt(50);
            int ratosAlt = OBJETOS.nextInt(25);
            if (cozinha[ratosAlt][ratosLarg] == '.' && cozinha2[ratosAlt][ratosLarg] != '^'
                    && cozinha[ratosAlt][ratosLarg] != '*') {
                cozinha2[ratosAlt][ratosLarg] = '^';
            } else if (cozinha[ratosAlt][ratosLarg] == '*') {
                if(ratosAlt - 2 >= 0 && ratosLarg - 1 >= 0){
                cozinha2[ratosAlt - 2][ratosLarg - 1] = '^';
                }else{
                    cozinha2[ratosAlt + 2][ratosLarg + 1] = '^';
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
            int novoIngredAlt, int novoIngredLarg, boolean buzina) {

        if (escolha == 1) {
            tempoRato = 3;
            iniciarCronometro();
            imprimir("Aperte 1 pra pegar de volta!");
            esperarTecla();
            if (segundosPassados() >= tempoRato) {
                imprimir("O rato pegou seu ingrediente...");
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
            if (y + 3 < ALTURA && x + 3 < LARGURA) {
                cozinha2[y + 3][x + 3] = '^';
            } else if(y - 3 >= 0 && x - 3 >= 0){
                cozinha2[y - 3][x - 3] = '^';
            }
        }
    }

    // Liga o Java às funções _kbhit e _getch do Windows (só usada no Windows)
    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);

        int _kbhit();

        int _getch();
    }

    static boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
    static boolean tecladoPreparado = false;

    // Retorna true se alguma tecla foi pressionada. NÃO espera: se nenhuma
    // tecla foi apertada, retorna false na hora e o jogo continua.
    public static boolean pressionouTecla() {
        if (windows) {
            return Crt.INSTANCE._kbhit() != 0;
        }
        prepararTecladoMacLinux();
        try {
            return System.in.available() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Retorna o código ASCII da tecla pressionada (ex.: 'a', 'w', 27 = ESC).
    // Se nenhuma tecla foi apertada, ESPERA até o usuário apertar uma.
    public static int obtemTeclaPressionada() {
        if (windows) {
            return Crt.INSTANCE._getch();
        }
        prepararTecladoMacLinux();
        try {
            return System.in.read();
        } catch (Exception e) {
            return -1;
        }
    }

    // Mostra "Pressione uma tecla para continuar..." e PARA o programa
    // até o jogador apertar qualquer tecla.
    //
    // Exemplo:
    // System.out.println("GAME OVER");
    // esperarTecla();
    public static void esperarTecla() {
        System.out.println("Pressione uma tecla para continuar...");
        obtemTeclaPressionada(); // fica esperando a tecla
    }

    // Mac/Linux: faz o terminal entregar cada tecla na hora (sem ENTER) e
    // sem mostrar a letra na tela. Quando o programa termina, volta ao normal.
    static void prepararTecladoMacLinux() {
        if (tecladoPreparado) {
            return;
        }
        tecladoPreparado = true;
        executarStty("-icanon -echo -icrnl min 1"); // -icrnl: ENTER vira 13, igual ao Windows
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executarStty("sane")));
    }

    static void executarStty(String opcoes) {
        try {
            new ProcessBuilder("sh", "-c", "stty " + opcoes + " < /dev/tty").start().waitFor();
        } catch (Exception e) {
            // se não conseguir, o teclado só vai funcionar com ENTER
        }
    }

    // Pausa o programa por um tempo, em milissegundos (1000 = 1 segundo).
    public static void esperar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            // não faz nada: se a espera for interrompida, o programa só continua
        }
    }

    // Guarda o momento em que o cronômetro começou (em milissegundos)
    static long inicioCronometro = 0;

    // Começa (ou recomeça) a contar o tempo do zero.
    public static void iniciarCronometro() {
        inicioCronometro = System.currentTimeMillis();
    }

    // Retorna quantos segundos passaram desde o iniciarCronometro().
    // O valor tem casas decimais: 1.5 quer dizer um segundo e meio.
    public static double segundosPassados() {
        long agora = System.currentTimeMillis();
        return (agora - inicioCronometro) / 1000.0;
    }

    public static void main(String[] args) {

        int ingredientes = 0;
        char cozinha[][];
        char cozinha2[][];
        cozinha = new char[25][50];
        cozinha2 = new char[25][50];
        boolean buzina = false;
        int novoIngredAlt = 0;
        int novoIngredLarg = 0;
        int escolha = 0;

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
        esperar(100); // espera 100 milissegundos -> 0,1 segundo

        switch (lanche) {
            case 1:
                ingredTotal = 4;
                tempoLimite = 40;
                colocarIngredientes(cozinha, ingredTotal);
                colocarRatos(cozinha, cozinha2, 65);
                imprimir("Você tem 40 segundos para pegar 4 ingredientes!");
                iniciarCronometro(); //cronômetro começa a contar

                break;
            case 2:
                ingredTotal = 6;
                tempoLimite = 50;
                colocarIngredientes(cozinha, ingredTotal);
                colocarRatos(cozinha, cozinha2, 75);
                colocarBuzina(cozinha, 1);
                imprimir("Você tem 50 segundos para pegar 6 ingredientes!");
                iniciarCronometro();

                break;
            case 3:
                ingredTotal = 9;
                tempoLimite = 60;
                colocarIngredientes(cozinha, ingredTotal);
                colocarRatos(cozinha, cozinha2, 100);
                colocarBuzina(cozinha, 1);
                imprimir("Você tem 1 minuto para pegar 9 ingredientes!");
                iniciarCronometro();

                break;
            default:
                imprimir("Inválido!");
                break;
        }

        BibliotecaTCA.esperar(1000); //espera 1 segundo pra começar o jogo
        BibliotecaTCA.limparTela();
        System.out.println("Mova o personagem H com W, A, S, D. ESC para sair.");

        while (true) {

            if (segundosPassados() >= tempoLimite) { //se o tempo acabar
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
                esperarTecla();
                return;
            }

            // Lógica para detectar o movimento
            if (pressionouTecla()) {
                int tecla = obtemTeclaPressionada();
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
                if (cozinha[y][x] == '&') { //se a posição atual do personagem tem a buzina
                    buzina = true; //buzina vira true porque o personagem a pegou
                    cozinha[y][x] = '.'; //onde ela estava, vira mapa normal
                } else if (cozinha[y][x] == '*') {
                    ingredientes++;
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
                            "2- Use a buzina");
                    escolha = lerValorInteiro();
                    pegouNaoPegou(escolha, tecla, ingredientes, cozinha, cozinha2, novoIngredAlt, novoIngredLarg,
                            buzina);
                }
            }
        }
    }
}