# SwiftBot Robotics System

SwiftBot Robotics System is a Java-based interactive **Simon Game** developed as a university coursework project.  
The game runs on a physical **SwiftBot robot**, combining CLI interaction with robot lights, buttons, and movement.

This repository is published as a **portfolio project** to demonstrate robotics interaction, event-driven input handling, and game logic implementation using the SwiftBot SDK.

---

## Features

- Randomly generated color sequences (Simon-style gameplay)
- Visual feedback using SwiftBot underlights
- User input via SwiftBot physical buttons (A / B / X / Y)
- Real-time correctness checking and game-over detection
- Score tracking with highest score feedback
- Celebration behavior (robot movement and lighting) upon reaching higher scores
- Command-line prompts guiding the user through gameplay

---

## Tech Stack & Requirements

- **Language:** Java (Java SE 8)
- **Build Tool:** Maven
- **SDK:** SwiftBot SDK `5.1.3`
- **Operating System:**  
  - macOS (used during development)  
  - Windows (expected to work, not fully tested)
- **Hardware Requirement:**  
  ⚠️ **A physical SwiftBot robot is required to run this project**

---

## How It Works

At a high level, the workflow is as follows:

1. Connect the SwiftBot robot to your machine
2. Deploy the Java program to the SwiftBot environment
3. Authenticate and establish a connection with the robot
4. Run the program and interact via:
   - Terminal (CLI prompts)
   - SwiftBot buttons and underlights

> Exact deployment and authentication steps depend on the SwiftBot SDK setup and hardware configuration.

---

## Gameplay & Controls

- The robot displays a sequence of colors using its underlights
- The player must repeat the sequence using SwiftBot buttons
- Each correct round increases the sequence length
- An incorrect input results in **Game Over**
- Scores are displayed in the terminal
- Higher scores trigger a celebration sequence involving movement and lights

---

## Project Structure