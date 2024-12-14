# Evolution Simulation

This is a Java-based simulation project that models a virtual world where creatures can interact, reproduce, and die based on probabilities. The simulation is designed to be extensible, allowing for future enhancements such as movement, eating, and sensing.

## Features
- **Creature Class**: Represents individual creatures with attributes like health, chance to die, and chance to reproduce.
- **World Class**: Manages the environment, spawning creatures, and running the simulation cycles.
- **Attributes Interface**: Defines the core attributes for creatures, making the design modular and extensible.
- **Dynamic Name Generation**: Reads creature names from `names.txt`, allowing for customizable naming.

## How to Use
1. **Setup**: Ensure you have Java installed on your system and create a `names.txt` file in the project directory with one name per line.
   Example `names.txt`:

"Alpha Beta Gamma Delta"

2. **Compile**: Open a terminal, navigate to the project folder, and compile the code:

3. **Run**: Run the simulation:

4. **Output**: The program will display the creatures' actions (e.g., reproducing, dying) and world events over a specified number of cycles.

## Example Output
Starting Simulation... === Cycle 1 === Alpha has reproduced. New creature: Gamma Beta has died. === Cycle 2 === Gamma has reproduced. New creature: Delta A new creature (Theta) has spawned! === Cycle 3 === Delta has died.

## File Structure
- **EvolutionSimulation.java**: Main simulation code.
- **names.txt**: File containing names for dynamically generated creatures.

## Future Enhancements
- Add interactions between creatures (e.g., predators and prey).
- Introduce food mechanics and health regeneration.
- Expand creature attributes to include traits like speed, energy, and sensing capabilities.

## Requirements
- Java 8 or higher.
- A text file named `names.txt` in the project directory for dynamic name generation.

## License
This project is open-source and available for educational and personal use. Feel free to modify and extend it as needed!
