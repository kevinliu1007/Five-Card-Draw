import Game.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by kevinliu1007 on 9/8/17.
 */

public class FiveCardDraw {

    /**
     * Main method for project 1.
     *
     * @param argv argv
     */
    public static void main(String[] argv) {
        boolean cont = true;

        /* Main program orchestration */
        while (cont){
            Game game = new Game();

            /* Phases of the game */
            game.opponentSelection();
            game.dealingPhase();
            game.discardPhase();
            game.comparePhase();

            System.out.print("\nDo you wish to play another one? (yes/no) \n");

            /* Get user input to see if user want to continue */
            try {
                boolean validInput = false;

                while (!validInput) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
                    Scanner scanner = new Scanner(buffer.readLine());

                    if (scanner.hasNext()) {
                        validInput = true;
                        String response = scanner.next();

                        if (response.equals("no")) {
                            cont = false;
                        } else if (response.equals("yes")) {
                            System.out.println("Starting a new round...\n");
                        } else {
                            validInput = false;
                            System.out.println("Please enter a valid respond");
                        }
                    } else {
                        System.out.println("Please enter a respond");
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        System.out.println("\nThank you for playing!!!\n");
    }
}
