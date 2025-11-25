import { useState, useEffect } from "react";
import { Box, Container, Flex, Grid, Spinner, Text, Center, Kbd } from "@chakra-ui/react";
import { Toaster, toast } from "react-hot-toast";
import { ColorModeButton, useColorMode } from "./components/ui/color-mode";
import { SearchBar } from "./components/SearchBar";
import { WeatherCard } from "./components/WeatherCard";
import { WeatherHighlights } from "./components/WeatherHighlights";
import { fetchWeatherData } from "./services/weatherService";

function App() {
  const [weatherData, setWeatherData] = useState(null);
  const [loading, setLoading] = useState(false);
  const { toggleColorMode } = useColorMode();

  const handleSearch = async (city) => {
    setLoading(true);
    // Dismiss any existing toasts
    toast.dismiss();
    try {
      const data = await fetchWeatherData(city);
      setWeatherData(data);
    } catch (err) {
      console.error(err);
      toast.error(err.message || "An unexpected error occurred.");
      // Clear data on error if desired, or keep previous data
      setWeatherData(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    handleSearch("pune");
  }, []);

  useEffect(() => {
    const handleKeyDown = (event) => {
      if (event.code === "Space" && document.activeElement.tagName !== "INPUT") {
        event.preventDefault();
        toggleColorMode();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [toggleColorMode]);

  return (
    <Flex minH="100vh" bg="gray.100" _dark={{ bg: "gray.900" }} align="center" justify="center" p={4} direction="column">
      <Toaster position="top-center" reverseOrder={false} />
      <Container maxW="900px">
        <Box position="relative" mb={8}>
          <Flex justify="center">
            <SearchBar onSearch={handleSearch} />
          </Flex>
          <Box position="absolute" right={0} top="50%" transform="translateY(-50%)">
            <ColorModeButton bg="white" _dark={{ bg: "gray.800" }} outline="none" />
          </Box>
        </Box>

        {loading ? (
          <Center h="300px">
            <Spinner size="xl" color="blue.500" />
          </Center>
        ) : weatherData ? (
          <Grid
            templateColumns={{ base: "1fr", md: "400px 1fr" }}
            gap={6}
            alignItems="stretch"
          >
            <Box>
              <WeatherCard data={weatherData} />
            </Box>
            <Box>
              <WeatherHighlights data={weatherData} />
            </Box>
          </Grid>
        ) : (
          <Center h="300px">
            <Text color="gray.500">Search for a city to see the weather</Text>
          </Center>
        )}
      </Container>

      <Box mt={10} textAlign="center">
        <Text
          fontSize="sm"
          color="gray.500"
          _dark={{ color: "gray.400" }}
          animation="pulse 3s infinite"
          fontWeight="medium"
          letterSpacing="wide"
        >
          Press <Kbd borderColor="gray.400">Space</Kbd> to toggle Day/Night theme
        </Text>
      </Box>
      <style>
        {`
          @keyframes pulse {
            0% { opacity: 1; }
            50% { opacity: 0.5; }
            100% { opacity: 1; }
          }
        `}
      </style>
    </Flex>
  );
}

export default App;
