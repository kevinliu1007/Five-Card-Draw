import Game.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by kevinliu1007 on 9/8/17.
 */

public class FiveCardDraw {

    public static void main(String[] argv) {
        boolean cont = true;

        while (cont){
            Game game = new Game();

            game.opponentSelection();
            game.dealingPhase();
            game.discardPhase();
            game.comparePhase();

            System.out.print("\nDo you wish to play another one? (yes/no) \n");

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
    }
}
