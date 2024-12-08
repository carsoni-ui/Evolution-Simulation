import java.io.*;
import java.util.*;

// Attributes Interface (3rd class/interface)
interface Attributes {
    double getHealth();
    double getChanceToDie();
    double getChanceToReproduce();
}

// Creature Class
class Creature implements Attributes {
    private String name;
    private double health; // Health level
    private double chanceToDie; // Probability of dying
    private double chanceToReproduce; // Probability of reproducing

    public Creature(String name, double health, double chanceToDie, double chanceToReproduce) {
        this.name = name;
        this.health = health;
        this.chanceToDie = chanceToDie;
        this.chanceToReproduce = chanceToReproduce;
    }

    public String getName() {
        return name;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public double getChanceToDie() {
        return chanceToDie;
    }

    @Override
    public double getChanceToReproduce() {
        return chanceToReproduce;
    }

    public boolean die() {
        double roll = Math.random();
        return roll < chanceToDie; // Returns true if the creature dies
    }

    public boolean reproduce() {
        double roll = Math.random();
        return roll < chanceToReproduce; // Returns true if the creature reproduces
    }

    @Override
    public String toString() {
        return "Creature{name='" + name + "', health=" + health + "}";
    }
}

// World Class
class World {
    private List<Creature> creatures = new ArrayList<>();
    private double spawnChance; // Chance to spawn a new creature
    private Random random = new Random();

    public World(double spawnChance) {
        this.spawnChance = spawnChance;
    }

    public void createCreature(String name) {
        double health = 100; // Default health
        double chanceToDie = 0.1; // 10% chance to die
        double chanceToReproduce = 0.2; // 20% chance to reproduce
        Creature newCreature = new Creature(name, health, chanceToDie, chanceToReproduce);
        creatures.add(newCreature);
    }

    public void spawnFood() {
        System.out.println("Food spawned in the world!");
    }

    public void simulateCycle() {
        Iterator<Creature> iterator = creatures.iterator();
        List<Creature> newCreatures = new ArrayList<>(); // Temporary list for new creatures

        while (iterator.hasNext()) {
            Creature creature = iterator.next();

            // Check if the creature dies
            if (creature.die()) {
                System.out.println(creature.getName() + " has died.");
                iterator.remove(); // Safely remove while iterating
            } else if (creature.reproduce()) {
                String newName = getRandomName();
                System.out.println(creature.getName() + " has reproduced. New creature: " + newName);
                newCreatures.add(new Creature(newName, 100, 0.1, 0.2));
            }
        }

        // Add all new creatures after iteration
        creatures.addAll(newCreatures);

        // Chance to spawn a new random creature
        if (Math.random() < spawnChance) {
            String randomName = getRandomName();
            System.out.println("A new creature (" + randomName + ") has spawned!");
            createCreature(randomName);
        }
    }

    private String getRandomName() {
        try (BufferedReader reader = new BufferedReader(new FileReader("names.txt"))) {
            List<String> names = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }
            return names.get(random.nextInt(names.size()));
        } catch (IOException e) {
            e.printStackTrace();
            return "Unnamed";
        }
    }

    public void startSimulation(int cycles) {
        for (int i = 0; i < cycles; i++) {
            System.out.println("=== Cycle " + (i + 1) + " ===");
            simulateCycle();
        }
    }
}

// Main Class
public class EvolutionSimulation {
    public static void main(String[] args) {
        // File validation for names.txt
        File file = new File("names.txt");
        if (!file.exists()) {
            System.err.println("Error: names.txt file not found. Please ensure it exists in the project directory.");
            return;
        }

        World world = new World(0.3); // 30% chance to spawn new creatures
        world.createCreature("Alpha");
        world.createCreature("Beta");

        System.out.println("Starting Simulation...");
        world.startSimulation(10); // Run 10 cycles of simulation
    }
}
