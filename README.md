# AutoScheduling Application

The **AutoScheduling Application** is a smart, Java-based tool designed to optimize your daily planning. This app allows users to input their tasks and events, specifying details such as:

- **Event Characteristics**:
  - Fixed (Rigid) or Flexible (Fluid) schedules.
  - Priorities: High, Medium, or Low.
  - Dependencies between events (e.g., "Task A must be completed before Task B").
  
The application then generates **three alternative schedules**, letting you select the one that best suits your needs. Once selected, the app provides options to:

1. Export the schedule as an **ICS file** for integration with calendar apps (like Apple Calendar).
2. Directly add the schedule to your **Google Calendar** using the Google Calendar API.

## Key Features
- **Intelligent Scheduling**:
  - Balances rigid and fluid events to create realistic and achievable schedules.
  - Respects user-defined priorities and event dependencies.

- **User-Friendly Output**:
  - Choose from three alternative schedules.
  - Export your schedule as an ICS file or sync it with Google Calendar.

- **Modern UI**:
  - Built with React.js for an intuitive and responsive user interface.
  - Fully integrated with a Java backend for seamless functionality.

- **Customizable Preferences**:
  - Set working hours, break times, and other personal scheduling preferences.

## Technology Stack
- **Backend**: Java (with Spring Boot for REST API development).
- **Frontend**: React.js (using modern component-based design).
- **Data Storage**: JSON or a database (e.g., MySQL/PostgreSQL) to persist events and settings.
- **Integration**: Google Calendar API for seamless schedule synchronization.

## How It Works
1. **Input Tasks and Events**: Provide details for your day, including deadlines, priorities, and dependencies.
2. **Generate Schedules**: The app creates three optimized schedules tailored to your inputs.
3. **Select and Export**: Pick your preferred schedule and either download an ICS file or sync it directly to Google Calendar.

## Use Cases
- **Busy Professionals**: Optimize your workday by balancing meetings, tasks, and personal time.
- **Students**: Plan study sessions, assignments, and exams with built-in prioritization.
- **Event Planners**: Manage overlapping events and resolve scheduling conflicts efficiently.

## Getting Started
### Prerequisites
- Java 17 or higher
- Node.js (for running the React frontend)
- Google Calendar API credentials (for syncing schedules)

### Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/autoscheduler.git
   ```
2. Navigate to the project directory:
   ```bash
   cd autoscheduler
   ```
3. Run the backend:
   ```bash
   cd backend
   ./gradlew bootRun
   ```
4. Run the frontend:
   ```bash
   cd frontend
   npm install
   npm start
   ```

### Usage
- Access the application at `http://localhost:3000`.
- Input your tasks and preferences.
- Generate, review, and select a schedule.
- Export or sync your schedule with ease!

---

Start optimizing your day with the **AutoScheduling Application**—because your time matters!
