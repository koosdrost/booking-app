# BVCG Reservation App

A Spring Boot application with Vue.js frontend for scraping and displaying available volleyball field reservations at BVCG.

## Recent Improvements

### Security Enhancements
- **Externalized credentials**: Moved hardcoded credentials to `application.properties` with environment variable support
- **Environment variables**: Credentials can now be set via `RESERVATION_USERNAME` and `RESERVATION_PASSWORD` environment variables
- **Configuration classes**: Created type-safe configuration properties for reservation and Selenium settings

### Code Quality
- **Replaced Thread.sleep**: All blocking sleep calls replaced with proper `WebDriverWait` conditions
- **Better error handling**: Added comprehensive error handling in the REST controller with proper HTTP status codes
- **Improved Selenium waits**: Using explicit waits for better reliability and performance
- **Logging improvements**: Enhanced logging throughout the application

### Frontend Enhancements
- **Complete AvailableSlots component**: Implemented the missing slots display component with:
  - Loading states with spinner
  - Error handling with retry functionality
  - Empty states with helpful messages
  - Beautiful card-based UI for displaying slots
  - Responsive design for mobile devices
- **Better UX**: Added search button to trigger slot fetching on-demand
- **API integration**: Updated to use new `/api/*` endpoints
- **Enhanced LogPanel**: Better error handling and visual feedback

### API Improvements
- **RESTful structure**: All endpoints now under `/api` prefix
- **Consistent responses**: Standardized JSON response format
- **Health endpoint**: Added `/api/health` for monitoring
- **Better error responses**: Proper HTTP status codes and error messages

### Configuration
- **CORS setup**: Proper CORS configuration for frontend-backend communication
- **Flexible Selenium options**: Headless mode and timeouts now configurable via properties
- **Vite proxy**: Updated proxy configuration for seamless API communication

## Setup

### Prerequisites
- Java 21
- Maven
- Node.js (for frontend)
- Chrome/Chromium browser (for Selenium)

### Backend Setup

1. Set up your credentials (choose one method):

   **Option A: Environment Variables (Recommended)**
   ```bash
   export RESERVATION_USERNAME=your-email@example.com
   export RESERVATION_PASSWORD=your-password
   ```

   **Option B: .env file**
   ```bash
   cp .env.example .env
   # Edit .env with your actual credentials
   ```

   **Option C: application.properties**
   Edit `src/main/resources/application.properties` and update the default values

2. Build and run the backend:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will start on http://localhost:8080

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd reservation-frontend
   ```

2. Install dependencies (if needed):
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will start on http://localhost:5173

## Usage

1. Open http://localhost:5173 in your browser
2. Click the "Search" button in the Available Slots panel
3. Watch the log messages in the left panel as the scraper runs
4. View available slots in the right panel

## API Endpoints

- `GET /api/available-slots` - Fetch available reservation slots
- `GET /api/logs` - Get scraper log messages
- `POST /api/reset` - Reset search state and clear logs
- `GET /api/health` - Health check endpoint

## Configuration Options

### Application Properties
Located in `src/main/resources/application.properties`:

```properties
# Reservation settings
reservation.login.url=https://bvcg.nl/mijn-account/
reservation.booking.url=https://bvcg.nl/veld-reserveren/
reservation.username=${RESERVATION_USERNAME:default@example.com}
reservation.password=${RESERVATION_PASSWORD:changeme}

# Selenium settings
selenium.headless=true
selenium.timeout.implicit=10
selenium.timeout.explicit=10

# CORS settings
cors.allowed.origins=http://localhost:5173,http://localhost:3000,http://localhost:8080
```

## Security Notes

- **Never commit credentials**: The `.env` file and actual credentials should never be committed to version control
- **Use environment variables**: In production, always use environment variables for sensitive data
- **Update .gitignore**: Ensure `.env` is listed in `.gitignore`

## Architecture

### Backend (Spring Boot)
- **ReservationService**: Core scraping logic using Selenium WebDriver
- **ReservationController**: REST API endpoints
- **Configuration Classes**: Type-safe configuration management
- **WebConfig**: CORS and web configuration

### Frontend (Vue.js)
- **App.vue**: Main application layout
- **LogPanel.vue**: Real-time log display with auto-refresh
- **AvailableSlots.vue**: Slot search and display with loading/error states

## Future Improvements

Potential enhancements:
- Database persistence for historical data
- User authentication and authorization
- Automatic periodic scraping with notifications
- More configurable search parameters (different days, time ranges)
- Booking functionality integration
- Unit and integration tests
- Docker containerization
- CI/CD pipeline

## License

Private project for BVCG volleyball field reservations.
