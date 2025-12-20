import java.util.*;

public class Main {

    public static void main(String[] args) {
        Estoque estoque = new Estoque("Relogio", "Saia", "Sandalia", "Tênis", "Controle Video-Game", 15);
        
        System.out.println("Selecione uma opção:");
        
        Menu menu = new Menu(estoque);
        menu.exibirMenu();

    }
}
