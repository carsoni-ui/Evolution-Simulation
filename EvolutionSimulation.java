import java.io.*;
import java.util.*;

// Attributes Interface
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
    private double metabolism; // Rate at which health decreases
    private Random random = new Random();

    public Creature(String name, double health, double chanceToDie, double chanceToReproduce, double metabolism) {
        this.name = name;
        this.health = health;
        this.chanceToDie = chanceToDie;
        this.chanceToReproduce = chanceToReproduce;
        this.metabolism = metabolism;
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
        // Health decreases due to metabolism
        health -= metabolism;
        if (health <= 0) {
            health = 0;
            return true;
        }
        // Additional death chance based on health
        double deathChance = chanceToDie * (1 - (health / 100));
        double roll = Math.random();
        return roll < deathChance;
    }

    public boolean reproduce() {
        // Reproduction chance depends on health
        if (health < 70) { // Increased health threshold
            return false; // Not healthy enough to reproduce
        }
        double roll = Math.random();
        if (roll < chanceToReproduce) {
            double reproductionCost = 10.0; // Health cost for reproduction
            decreaseHealth(reproductionCost);
            return true;
        }
        return false;
    }

    public void consumeFood(Food food) {
        health += food.getNutrition();
        if (health > 100) {
            health = 100; // Cap health at 100
        }
    }

    public void decreaseHealth(double amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    @Override
    public String toString() {
        return "Creature{name='" + name + "', health=" + String.format("%.2f", health) + "}";
    }
}

// Food Class
class Food {
    private String type;
    private double nutrition; // Amount of health it restores

    public Food(String type, double nutrition) {
        this.type = type;
        this.nutrition = nutrition;
    }

    public String getType() {
        return type;
    }

    public double getNutrition() {
        return nutrition;
    }

    @Override
    public String toString() {
        return "Food{type='" + type + "', nutrition=" + String.format("%.2f", nutrition) + "}";
    }
}

// World Class
class World {
    private List<Creature> creatures = new LinkedList<>();
    private List<Food> foods = new LinkedList<>();
    private double spawnChance; // Chance to spawn a new creature
    private double foodSpawnChance; // Chance to spawn food each cycle
    private Random random = new Random();
    private static final int MAX_POPULATION = 1000; // Maximum allowed population
    private static final double ADDITIONAL_HEALTH_LOSS = 10.0; // Health lost when no food is found
    private static final int FOOD_SPAWN_SEASONAL_ADJUSTMENT_CYCLE = 10; // Cycle interval for seasonal changes

    public World(double spawnChance, double foodSpawnChance) {
        this.spawnChance = spawnChance;
        this.foodSpawnChance = foodSpawnChance;
    }

    public void createCreature(String name) {
        double health = 100; // Default health
        double chanceToDie = 0.05; // 5% chance to die
        double chanceToReproduce = 0.3; // 30% chance to reproduce
        double metabolism = 5; // Health decreases by 5 each cycle
        Creature newCreature = new Creature(name, health, chanceToDie, chanceToReproduce, metabolism);
        creatures.add(newCreature);
    }

    public void spawnFood() {
        String[] foodTypes = {"Herb", "Berry", "Fruit"};
        String type = foodTypes[random.nextInt(foodTypes.length)];
        double nutrition = 20 + (random.nextDouble() * 30); // Nutrition between 20 and 50
        Food food = new Food(type, nutrition);
        foods.add(food);
        // Optionally, log food spawning
        // System.out.println("Food spawned: " + food);
    }

    public void simulateCycle(int cycleNumber) {
        System.out.println("\n=== Cycle " + cycleNumber + " ===");

        // Seasonal Change Example: Every 10 cycles, reduce food spawn chance by 20%
        if (cycleNumber % FOOD_SPAWN_SEASONAL_ADJUSTMENT_CYCLE == 0) {
            foodSpawnChance *= 0.8; // Reduce by 20%
            System.out.println("Seasonal change: Food spawn chance decreased to " + String.format("%.2f", foodSpawnChance));
        }

        // Spawn food based on chance
        if (Math.random() < foodSpawnChance) {
            spawnFood();
        }

        // Shuffle creatures to randomize consumption order
        Collections.shuffle(creatures, random);

        Iterator<Creature> creatureIterator = creatures.iterator();
        List<Creature> newCreatures = new ArrayList<>(); // Temporary list for new creatures
        int deathsThisCycle = 0;
        int reproductionsThisCycle = 0;
        int foodConsumedThisCycle = 0;

        while (creatureIterator.hasNext()) {
            Creature creature = creatureIterator.next();

            // Creature attempts to consume food
            if (!foods.isEmpty()) {
                int foodIndex = random.nextInt(foods.size());
                Food food = foods.remove(foodIndex); // Random food consumption
                creature.consumeFood(food);
                foodConsumedThisCycle++;
            } else {
                // No food available; creature loses additional health
                creature.decreaseHealth(ADDITIONAL_HEALTH_LOSS);
            }

            // Check if the creature dies
            if (creature.die()) {
                creatureIterator.remove(); // Safely remove while iterating
                deathsThisCycle++;
                continue;
            }

            // Check if the creature reproduces
            if (creature.reproduce()) {
                if (creatures.size() + newCreatures.size() < MAX_POPULATION) {
                    String newName = getRandomName();
                    newCreatures.add(new Creature(newName, 100, 0.05, 0.3, 5));
                    reproductionsThisCycle++;
                }
            }
        }

        // Add all new creatures after iteration
        creatures.addAll(newCreatures);

        // Chance to spawn a new random creature
        if (Math.random() < spawnChance && creatures.size() < MAX_POPULATION) {
            String randomName = getRandomName();
            createCreature(randomName);
            reproductionsThisCycle++;
        }

        // Calculate average, min, and max health
        double totalHealth = 0;
        double minHealth = Double.MAX_VALUE;
        double maxHealth = Double.MIN_VALUE;
        for (Creature creature : creatures) {
            totalHealth += creature.getHealth();
            if (creature.getHealth() < minHealth) {
                minHealth = creature.getHealth();
            }
            if (creature.getHealth() > maxHealth) {
                maxHealth = creature.getHealth();
            }
        }
        double averageHealth = creatures.isEmpty() ? 0 : totalHealth / creatures.size();

        // Summary after cycle
        System.out.println("--- Cycle " + cycleNumber + " Summary ---");
        System.out.println("Total Creatures: " + creatures.size());
        System.out.println("Deaths This Cycle: " + deathsThisCycle);
        System.out.println("Reproductions This Cycle: " + reproductionsThisCycle);
        System.out.println("Food Consumed This Cycle: " + foodConsumedThisCycle);
        System.out.println("Food Available: " + foods.size());
        System.out.println(String.format("Average Health: %.2f", averageHealth));
        if (!creatures.isEmpty()) {
            System.out.println(String.format("Health Range: %.2f - %.2f", minHealth, maxHealth));
        }
        System.out.println("--------------------------------\n");
    }

    private String getRandomName() {
        try (BufferedReader reader = new BufferedReader(new FileReader("names.txt"))) {
            List<String> names = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    names.add(line.trim());
                }
            }
            if (names.isEmpty()) {
                return "Unnamed";
            }
            return names.get(random.nextInt(names.size()));
        } catch (IOException e) {
            e.printStackTrace();
            return "Unnamed";
        }
    }

    public void startSimulation(int cycles) {
        for (int i = 1; i <= cycles; i++) {
            simulateCycle(i);
            if (creatures.isEmpty()) {
                System.out.println("All creatures have died. Simulation ending early.");
                break;
            }
        }
        System.out.println("\nSimulation ended. Final state:");
        displayWorldState();
    }

    private void displayWorldState() {
        System.out.println("\n--- Current World State ---");
        System.out.println("Creatures (" + creatures.size() + "):");
        for (Creature creature : creatures) {
            System.out.println(creature);
        }
        System.out.println("Food available (" + foods.size() + "):");
        for (Food food : foods) {
            System.out.println(food);
        }
        System.out.println("---------------------------\n");
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

        World world = new World(0.2, 0.7); // Increased foodSpawnChance from 0.5 to 0.7
        world.createCreature("Alpha");
        world.createCreature("Beta");
        world.createCreature("Gamma");

        System.out.println("Starting Simulation...");
        world.startSimulation(20); // Run 20 cycles of simulation
    }
}
