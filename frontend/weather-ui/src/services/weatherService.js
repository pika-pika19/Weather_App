export const fetchWeatherData = async (city) => {
  try {
    const response = await fetch(
      `http://192.168.0.101:8080/api/v1/weather?city=${city}`
    );
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("City not found. Please check the spelling.");
      }
      throw new Error("Failed to fetch weather data. Please try again later.");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching weather data:", error);
    // Re-throw the error so it can be caught by the component
    throw error;
  }
};

