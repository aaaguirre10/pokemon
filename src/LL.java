public class LL {
    LLNode head;
    LLNode tail;
    int size;

    public LL(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public LL(LLNode pkmn){
        this.head = pkmn;
        this.tail = pkmn;
        this.size = 1;
    }

    public LLNode getStarterPokemon(int idx){
        LLNode temp = this.head;
        int curridx = 0;

        while(temp != null && curridx != idx){
            temp = temp.next;

        }

        return temp;
    }

    
    /**
     * This method will recursively traverse the linked list and return the Pokémon we are searching for
     * if the Pokémon is not in the party, it will simply return an array with an empty string.
     * 
     * Must be done recursively
     *
     * @param currNode
     * @param getPokemon
     * @return the String array representing the pokemon we were looking for in the linked list
     *  or an empty String array if the Pokemon is not in the party
     */
    public String[] getFromParty(LLNode headNode, String getPokemon) {
        if (headNode == null) {
            // If we reach the end of the list without finding the Pokemon, return an empty string array
            return new String[0];
        } else if (headNode.pokemon[1].equals(getPokemon)) {
            // If the current node is the Pokemon we're searching for, return its details in a string array
            return headNode.pokemon;
        } else {
            // Recursively search for the Pokemon in the next node
            return getFromParty(headNode.next, getPokemon);
        }
    }
    

    /**
     * This method will add a Pokémon to the party at the end and increase the size count of the linked list
     * Don't forget to update the size of the linked list when adding a Pokemon to the party
     * @param pkmn
     * @return void
     */
    public void addToParty(LLNode pkmn){
        if (this.head == null) {
            // If the linked list is empty, set both the head and tail to the new Pokemon node
            this.head = pkmn;
            this.tail = pkmn;
        } else {
            // Otherwise, add the new Pokemon node to the end of the list and update the tail
            this.tail.next = pkmn;
            this.tail = pkmn;
        }
        // Increment the size count of the linked list
        this.size++;
    } 

    /**
     * This method will remove a Pokémon from the party, regardless of its position in the list, and decrease the size count of the linked list
     * Don't forget to update the size of the linked list when removing a Pokemon
     * @param pkmnName
     * @return the String array representing the Pokemon being removed from the party (linked list)
     */
    public String[] removeFromParty(String pkmnName){
        if(head == null){
            // If the list is empty, return an empty string array
            return new String[0];
        }else if(head.pokemon[1].equals(pkmnName)){
            // If the head node contains the Pokemon we want to remove
            String[] removedPkmn = head.pokemon;
            head = head.next; // Set the head node to the next node
            size--;
            if(size == 0){
                tail = null; // If the list is empty, set the tail to null as well
            }
            return removedPkmn;
        }else{
            // If the Pokemon is not in the head node, search for it in the rest of the list
            LLNode currNode = head.next;
            LLNode prevNode = head;
            while(currNode != null){
                if(currNode.pokemon[1].equals(pkmnName)){
                    // If the current node contains the Pokemon we want to remove
                    String[] removedPkmn = currNode.pokemon;
                    prevNode.next = currNode.next; // Link the previous node to the next node
                    size--;
                    if(currNode == tail){
                        tail = prevNode; // If the removed node is the tail, update the tail node as well
                    }
                    return removedPkmn;
                }
                // Move on to the next node
                prevNode = currNode;
                currNode = currNode.next;
            }
            // If we reach the end of the list without finding the Pokemon, return an empty string array
            return new String[0];
        }
    }

 
    /**
     * This method will traverse through the linked list recursively and display all of the Pokémon's name 
     * and their level in it in order from first to last
     * 
     * Must be done recursively
     * 
     * @param headNode
     * @return void, the method will only print the Pokemon names and levels in order
     */
    public void displayParty(LLNode headNode){
        if (headNode == null) {
            // If we reach the end of the list without finding the Pokemon, return an empty string array

            return;
        } else {
            // Recursively search for the Pokemon in the next node
            String partyList = headNode.pokemon[1] + " Lvl(" + headNode.pokemon[5] + ")";
            System.out.print(partyList);
            if(headNode.next != null){
                System.out.print(" -> ");
            }
            displayParty(headNode.next);
        }
    }


    /**
     * This method checks if a given Pokémon is in the party or not
     * 
     * @param pokemonName
     * @return boolean representing whether the input Pokemon is in the party or not
     */
    public boolean inParty(String pokemonName){
        LLNode currNode = this.head;
        while (currNode != null) {
            if (currNode.pokemon[1].equals(pokemonName)) {
                return true;
            }
            currNode = currNode.next;
        }
        return false; // If we traverse the entire list without finding the Pokémon, return false
    }
}
