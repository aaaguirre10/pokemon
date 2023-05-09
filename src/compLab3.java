import java.io.File;
import java.io.FileNotFoundException;
import java.security.Policy;
import java.util.Scanner;

// Optional libraries to play music
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class compLab3 {
    private static final int PARTY_SIZE = 6;
    private static String[][] pokemonBox;
    private static String[][] pokemonParty;

    public static void main(String[] args) throws Exception {

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // You can ignore this section, it is just to play music, if you don't want music to be played, you can delete this part
        playBackgroundMusic("pokemonCenter.wav");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        pokemonBox = getPokemonData(); //gets data from txt file
        pokemonParty = new String[PARTY_SIZE][]; //CONST 6 pokemon in party
        Scanner scanner = new Scanner(System.in); //scanner
        String playerName = getPlayerName(scanner); //gets player name

        int randomPokemon =  (int) (Math.random() * (pokemonBox.length-1)) + 1; //in order to get random starter pokemon in the party
        String[] pkm = getStarterPokemon(pokemonBox,randomPokemon);
        while(pkm.length <=1){ //ignores error where the pokemon was not starting in party
            randomPokemon =  (int) (Math.random() * (pokemonBox.length-1)) + 1;
            pkm = getStarterPokemon(pokemonBox,randomPokemon);
        }
        
        // initilize LL
        LLNode starterPokemon = new LLNode(pkm);
        LL linkedList = new LL(starterPokemon);

        System.out.println("Welcome to the Pokemon game, " + playerName + "!");
        int menuChoice;

        do {
            System.out.println("Welcome to the Pokemon Storage System!");
            System.out.println("Please select an option:");
            System.out.println("1. View Box");
            System.out.println("2. View Party");
            System.out.println("3. Deposit Pokemon");
            System.out.println("4. Withdraw Pokemon");
            System.out.println("5. View Pokemon stats");
            System.out.println("6. Log out\n");
            

            menuChoice = scanner.nextInt();
            scanner.nextLine(); // consume the remaining newline character


            switch (menuChoice) {
                case 1:
                    filterPokemon(pokemonBox, scanner);
                    break;
                case 2:
                    viewParty(linkedList, scanner);
                    break;
                case 3:
                    depositPokemon(linkedList, scanner);
                    break;
                case 4:
                    addToParty(linkedList, scanner);
                    break;
                case 5:
                    System.out.println("NAH GGS");
                break;
                case 6:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        } while (menuChoice != 6);

        System.out.println("Thank you for using the Pokemon Storage System!");

    } //end of main

    public static void playBackgroundMusic(String fileName) {
        try { // If the .wav file is found it will play the song while running the program
            File musicPath = new File("pokemonCenter.wav");
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            // Check if the clip is playing
            if (clip.isRunning()) {
                System.out.println("Music is playing.");
            } else {
                System.out.println("Music is not playing.");
            }
            System.out.println("Now Playing: \"Pokémon Center\" from Pokémon Colosseum\n");
        } catch (Exception e) { // If the file is not found, then there won't be any music while running the program.
        }
    }

    public static String[] getStarterPokemon(String[][] pokemonBox, int idx){
        return  pokemonBox[idx];
    }

    public static void viewParty(LL linkedList, Scanner scanner){
        System.out.println("--------------------------------------------------");
        System.out.println("Number of Pokemon in your party: " + linkedList.size);
        System.out.println("--------------------------------------------------");
        System.out.println("--- Pokemon Party ---");
        linkedList.displayParty(linkedList.head);
        System.out.println("");
        System.out.println("Enter the pokemon from your party:");
        String pokemonName = scanner.nextLine();

        String[] pokemonDetails = linkedList.getFromParty(linkedList.head, pokemonName);
        if (pokemonDetails.length > 0) {
            // Pokemon found in the party
            System.out.println("--------------------------------------------------");
            System.out.println("Pokemon found: " + pokemonDetails[1] + "(Lvl: " + pokemonDetails[5] + ")");
            System.out.println("--------------------------------------------------");
        } else {
            // Pokemon not found in the party
            System.out.println("--------------------------------------------------");
            System.out.println("Pokemon not found in the party.");
            System.out.println("--------------------------------------------------\n");
        }
    }

    public static void depositPokemon(LL linkedList, Scanner scanner){
        System.out.println("--------------------------------------------------");
        System.out.println("Who do you want to remove from party? (input the name of the Pokemon)");
        System.out.println("--- Pokemon Party ---");
        linkedList.displayParty(linkedList.head);
        System.out.println("");
        String pokemonName = scanner.nextLine();

        if(linkedList.size == 1){
            System.out.println("Cannot remove the last Pokemon from the party.");
        } else {
            String[] pokemonDetails = linkedList.removeFromParty(pokemonName);
            if (pokemonDetails.length > 0) {
                // Pokemon found in the party
                System.out.println("--------------------------------------------------");
                System.out.println("Pokemon was removed: " + pokemonDetails[1] + "(Lvl: " + pokemonDetails[5] + ")");
                System.out.println("--------------------------------------------------");
            } else {
                // Pokemon not found in the party
                System.out.println("--------------------------------------------------");
                System.out.println("Pokemon not found in the party.");
                System.out.println("--------------------------------------------------\n");
            }
        }
    }

    public static void addToParty(LL linkedList, Scanner scanner){
        System.out.println("--------------------------------------------------");
        System.out.println("Who do you want to add to the party? (input the name of the Pokemon)");
        System.out.println("--- Pokemon Party ---");
        linkedList.displayParty(linkedList.head);
        System.out.println("");
        String pokemonName = scanner.nextLine();

        String[] pkmSearch = searchPokemon(pokemonName);
        if (pkmSearch.length == 0) {
            System.out.println("Invalid Pokemon");
        } else {
            LLNode pkmToAdd = new LLNode(pkmSearch);
            linkedList.addToParty(pkmToAdd);
            System.out.println("Succesfully Added: " + pokemonName);
        }
    }

    public static String[] searchPokemon(String pkm){
        for(int i = 0; i < pokemonBox.length; i++){
            if (pokemonBox[i].length > 1 && pokemonBox[i][1].equals(pkm)){
                return pokemonBox[i];
            }
        }
        return new String[0];
    }
    
    public static String[][] getPokemonData() {
        try {
            File file = new File("src/pokemonList.txt");
            Scanner scanner = new Scanner(file);
            int numLines = 0;
    
            // count number of lines in file
            while (scanner.hasNextLine()) {
                numLines++;
                scanner.nextLine();
            }
    
            // initialize array with correct size
            String[][] pokemonBox = new String[numLines - 1][1000];
            scanner = new Scanner(file);
            scanner.nextLine(); // skip header
    
            // read data into array
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                pokemonBox[i] = values;
                i++;
            }
    
            scanner.close();
            return pokemonBox;
        } catch (FileNotFoundException e) {
            System.out.println("Error: pokemonList.txt not found.");
            return null;
        }
    }

    public static String getPlayerName(Scanner scanner) {
        System.out.println("Please enter your name:");
        return scanner.nextLine();
    }
    

    /**
     * This method displays all the Pokemon, given the type of filter, if no valid filter is given, display all Pokemon
     *
     * @param pokemonBox
     * @param filter
     * @return void
     */
    public static void displayPokemon(String[][] pokemonBox, String filter) {
        int numPokemon = 0;
        for (int i = 0; i < pokemonBox.length; i++) {
            if (pokemonBox[i][0] != null) {
                numPokemon++;
            }
        }
        String[][] newBox = new String[numPokemon][1000];
        int index = 0;
        for (int i = 0; i < pokemonBox.length; i++) {
            if (pokemonBox[i][0] != null) {
                newBox[index] = pokemonBox[i];
                index++;
            }
        }
        pokemonBox = newBox;
    
        System.out.println("Name    | Type 1 | Type 2 | Total | HP | Attack | Defense | Sp. Atk | Sp. Def | Speed | Generation | Legendary");
        System.out.println("--------|--------|--------|-------|----|--------|---------|---------|---------|-------|------------|----------");

        // Print out the filtered Pokemon
        for (int i = 0; i < pokemonBox.length; i++) {
            if (filter == null || filter.equalsIgnoreCase(pokemonBox[i][1])) {
                for (int j = 0; j < pokemonBox[i].length; j++) {
                    if (pokemonBox[i][j] != null) {
                        System.out.print(pokemonBox[i][j] + " ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static String[][] filterByType(String[][] pokemonBox, String type) {
        int filteredSize = 0;
        int maxColumns = 0;
    
        for (int i = 0; i < pokemonBox.length; i++) {
            if (pokemonBox[i].length > 2 && pokemonBox[i][2] != null && pokemonBox[i][2].equalsIgnoreCase(type)) {
                filteredSize++;
                maxColumns = Math.max(maxColumns, pokemonBox[i].length);
            }
        }

                System.out.println(filteredSize);

    
        String[][] filteredPokemon = new String[filteredSize][];
        int count = 0;
    
        for (int i = 0; i < pokemonBox.length; i++) {
            if (pokemonBox[i].length > 2 && pokemonBox[i][2] != null && pokemonBox[i][2].equalsIgnoreCase(type)) {
                filteredPokemon[count] = new String[maxColumns];
                filteredPokemon[count] = pokemonBox[i];
                count++;
            }
        }
    
        return filteredPokemon;
    }
    
    public static String[][] filterByLevel(String[][] pokemonBox, int level) {
        if (pokemonBox == null || pokemonBox.length < 2) {
            return new String[0][0];
        }

        int count = 0;

        // Count the number of matching Pokemon
        for (String[] pokemon : pokemonBox) {
            if (pokemon.length > 4 && Integer.parseInt(pokemon[4]) == level) {
                count++;
            }
        }

        // Create a new array with the matching Pokemon
        String[][] filteredPokemon = new String[count][pokemonBox[0].length];
        int index = 0;

        // Populate the new array with the matching Pokemon
        for (String[] pokemon : pokemonBox) {
            if (pokemon.length > 4 && Integer.parseInt(pokemon[4]) == level) {
                filteredPokemon[index] = pokemon;
                index++;
            }
        }

        return filteredPokemon;
    }

    public static String[][] filterByGeneration(String[][] pokemonBox, int generation) {
        if (pokemonBox == null || pokemonBox.length < 13) {
            return new String[0][0];
        }

        int count = 0;
    
        // Count the number of matching Pokemon
        for (String[] pokemon : pokemonBox) {
            if (pokemon.length > 12 && Integer.parseInt(pokemon[12]) == generation) {
                count++;
            }
        }
    
        // Create a new array with the matching Pokemon
        String[][] filteredPokemon = new String[count][pokemonBox[0].length];
        int index = 0;
    
        // Populate the new array with the matching Pokemon
        for (String[] pokemon : pokemonBox) {
            if (pokemon.length > 12 && Integer.parseInt(pokemon[12]) == generation) {
                filteredPokemon[index] = pokemon;
                index++;
            }
        }
    
        return filteredPokemon;
    }
    
    
    public static void filterPokemon(String[][] pokemonBox, Scanner scanner) {
        System.out.println("Please select a filter option:");
        System.out.println("1. Filter by type");
        System.out.println("2. Filter by level");
        System.out.println("3. Filter by generation");
        System.out.println("4. No Filter");
    
        int filterChoice = scanner.nextInt();
        scanner.nextLine(); // consume the remaining newline character
    
        switch (filterChoice) {
            case 1:
                System.out.println("Please enter a type to filter by:");
                String type = scanner.nextLine();
                String[][] filteredByType = filterByType(pokemonBox, type);
                displayPokemon(filteredByType, null); // Pass the filter option
                break;
            case 2:
                System.out.println("Please enter a level to filter by:");
                int level = scanner.nextInt();
                scanner.nextLine(); // consume the remaining newline character
                String[][] filteredByLevel = filterByLevel(pokemonBox, level);
                displayPokemon(filteredByLevel, null); // Pass null as filter option
                break;
            case 3:
                System.out.println("Please enter a generation to filter by:");
                int generation = scanner.nextInt();
                scanner.nextLine(); // consume the remaining newline character
                String[][] filteredByGeneration = filterByGeneration(pokemonBox, generation);
                displayPokemon(filteredByGeneration, null); // Pass null as filter option
                break;
            case 4:
                displayPokemon(pokemonBox, null); // Pass null as filter option
                break;
            default:
                System.out.println("Invalid choice. No filter will be applied.");
                break;
        }
    }
}
