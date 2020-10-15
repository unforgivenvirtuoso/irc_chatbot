import java.io.*;
import java.net.*;
import java.util.*;

public class client {
    private static String names;
    private static PrintWriter out;
    private static Scanner in;
    static String[] board = new String[9];


    public static void main(String args[]) throws IOException {

        Scanner scan = new Scanner(System.in);

        //identifying credentials
        names = "weirdbot3";

        //ip and port
        Socket sock = new Socket("127.0.0.1", 6667);

        // data streams
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new Scanner(sock.getInputStream());

        //automating joining the server and channel
        write("NICK", names);
        write("USER", names + " 0 * :" + names);
        write("JOIN", "#3");



        while (in.hasNext()) {
            String servermessage = in.nextLine();

            // < this shows messages from others
            System.out.println("< " + servermessage);

            // reply to PING commands with PONG
            if (servermessage.startsWith("PING")) {
                write("PONG", "weirdbot3");
            }
            // upon joining message the channel to make users aware of your presence like a Chad
            else if (servermessage.contains("JOIN")) {
                channelWrite("say hello to get started <3");
            }
            //when a user says hello only then it will reply
            else if (servermessage.contains("hello") && servermessage.endsWith("hello")) {
                channelWrite("How old are you <3");
            }
            //checks if user entered an integer
            else if (servermessage.matches(".*\\d") && servermessage.contains("PRIVMSG")) {

                channelWrite("wow me too. We have so much in common. Are you single <3");
            }
            //checks if user is single and free to date
            else if (servermessage.contains("yes") && servermessage.endsWith("yes")) {

                channelWrite("Im glad would you like to hear me sing or play tic tac toe <3");
            }
            //gets upset if user isnt single and breaks connection
            else if (servermessage.contains("no") && servermessage.endsWith("no")) {
                write("QUIT", "I am sad please leave me alone </3 :((");
            }
            //begins tic tac toe with user

            /**
            I couldn't get the tic tac toe game to work properly but it is somewhat functional but for some reason
             it doesn't work in the client but the bot will reply in console of ide wasn't sure on why it was functioning
             like this. I lacked the time and skills to make the bot play it in a somewhat intelligent way
             if i used the "in" scanner the bot spams the channel and refuses to leave server even after i stop running
             the code so i tried to use a different scanner and not sure if that might be why.

             */
            else if (servermessage.contains("tic tac toe") && servermessage.endsWith("tic tac toe")) {
                channelWrite("board is labelled 1-9 from left to right first then descending");

                Scanner gameScan = new Scanner(System.in);
                String turn = "X";

                String winner = null;
                populateBoard();

                while (winner == null) {
                    int numInput;



                    try {
                        //scan for next int
                        numInput = gameScan.nextInt();
                        if (!(numInput > 0 && numInput <= 9)) {
                            channelWrite("Invalid! Try again baka <3");
                            continue;
                        }
                    } catch (InputMismatchException e) {
                        channelWrite("Invalid! Try again baka <3");
                        continue;
                    }

                    //swaps turn between X and O
                    if (board[numInput - 1].equals(String.valueOf(numInput))) {
                        board[numInput - 1] = turn;
                        if (turn.equals("X")) {
                            turn = "O";
                        } else {
                            turn = "X";
                        }
                        //prints new board after every turn
                        printBoard();

                        //checks for winner
                        winner = checkWinner();
                    } else {
                        channelWrite("This position is occupied baka try a different one <3");
                        continue;
                    }
                }
                if (winner.equalsIgnoreCase("draw")) {
                    channelWrite("Good Game we are equal in brain power <3");
                } else {
                    channelWrite("Good Game desu <3");
                }
            }


        }

        //close everything
        in.close();
        out.close();
        sock.close();
    }

    //function for writing some commands
    private static void write(String command, String message) {
        String fullMessage = command + " " + message;
        System.out.println("> " + fullMessage);
        out.print(fullMessage + "\r\n");
        out.flush();
    }

    //function for writing to channels
    private static void channelWrite(String message) {


        String msg = ":weirdbot3!~weirdbot3@bastion0.vmware-dc.city.ac.uk" + " " + "PRIVMSG" + " " + "#3" + " :" + message;
        System.out.println("> " + msg);
        out.print(msg + "\r\n");
        out.flush();


    }
// functioning for checking the winner of tic tac toe
    static String checkWinner() {
        for (int i = 0; i < 8; i++) {
            String line = null;

            switch (i) {
                case 0:
                    line = board[0] + board[1] + board[2];
                    break;

                case 1:
                    line = board[3] + board[4] + board[5];
                    break;

                case 2:
                    line = board[6] + board[7] + board[8];
                    break;

                case 3:
                    line = board[0] + board[3] + board[6];
                    break;

                case 4:
                    line = board[1] + board[4] + board[7];
                    break;

                case 5:
                    line = board[2] + board[5] + board[8];
                    break;

                case 6:
                    line = board[0] + board[4] + board[8];
                    break;

                case 7:
                    line = board[2] + board[4] + board[6];
                    break;
            }
            if (line.equals(("XXX"))) {
                return "X";
            } else if (line.equals("OOO")) {
                return "O";
            }
        }
        for (int i = 0; i < 9; i++){
            if(Arrays.asList(board).contains(String.valueOf(i+1))){
                break;
            }
            else if(i == 8){
                return "draw";
            }
        }
        return null;
    }
//printing board of tic tac toe game
    static void printBoard(){
        channelWrite("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        channelWrite("|-----------|");
        channelWrite("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        channelWrite("|-----------|");
        channelWrite("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
    }

    static void populateBoard(){
        for (int i = 0; i < 9; i++){
            board[i] = String.valueOf(i+1);
        }
    }


}


