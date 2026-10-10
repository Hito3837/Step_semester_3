// Arena Battle Simulator — characters share ancestry, traps share only a capability.
// Warrior extends GameCharacter (it IS-A character with an id and a special move) and also
// implements Attackable + Defendable. Trap implements only Defendable with no relation to
// GameCharacter at all — having no getSpecialMove() or characterId is exactly correct, since
// the arena only ever needs the one shared defend() behavior from it. resolveDefense()
// depends purely on Defendable, so Warriors and Traps fight through one polymorphic loop.

interface Attackable {
    String attack();

    String attack(String weaponName);
}

interface Defendable {
    String defend();
}

abstract class GameCharacter {
    private static int nextCharacter = 1001;
    private final String characterId;
    private final String name;

    GameCharacter(String name) {
        this.characterId = "CHR-" + (nextCharacter++);
        this.name = name;
    }

    public abstract String getSpecialMove();

    String getCharacterId() {
        return characterId;
    }

    String getName() {
        return name;
    }
}

class Warrior extends GameCharacter implements Attackable, Defendable {
    public Warrior(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return getName() + " strikes with a blade";
    }

    @Override
    public String attack(String weaponName) {
        return getName() + " strikes with an " + weaponName;
    }

    @Override
    public String defend() {
        return getName() + " raises a shield";
    }

    @Override
    public String getSpecialMove() {
        return getName() + " unleashes Whirlwind Slash";
    }
}

class Trap implements Defendable {
    private final String trapType;

    public Trap(String trapType) {
        this.trapType = trapType;
    }

    @Override
    public String defend() {
        return trapType + " triggers automatically";
    }
}

public class ArenaBattleSimulator {
    static void resolveDefense(Defendable[] combatants) {
        for (Defendable combatant : combatants) {
            System.out.println(combatant.defend());
        }
    }

    public static void main(String[] args) {
        Warrior w = new Warrior("Kael");
        System.out.println(w.attack());
        System.out.println(w.attack("Iron Sword"));
        System.out.println(w.defend());
        System.out.println(w.getSpecialMove());

        Trap t = new Trap("Spike Pit");
        System.out.println(t.defend());

        resolveDefense(new Defendable[] { w, t });

        System.out.println(w.getCharacterId());
    }
}
