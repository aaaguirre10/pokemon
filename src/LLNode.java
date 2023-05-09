// Do not modify this file
public class LLNode {
    String[] pokemon;
    LLNode next;

    public LLNode(){
        this.pokemon = new String[1];
        this.next = null;
    }

    public LLNode(String[] pkmn){
        this.pokemon = pkmn;
        this.next = null;
    }
}
