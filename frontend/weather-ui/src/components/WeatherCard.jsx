import { Box, Text, Flex, Heading, Icon } from "@chakra-ui/react";
import { getWeatherIcon } from "../utils/weatherIcons";

export const WeatherCard = ({ data }) => {
  if (!data) return null;

  const WeatherIcon = getWeatherIcon(data.description);

  return (
    <Box
      bgGradient="to-br"
      gradientFrom="yellow.400"
      gradientTo="orange.400"
      borderRadius="3xl"
      p={8}
      color="gray.800"
      boxShadow="xl"
      position="relative"
      overflow="hidden"
      h="full"
      minH="400px"
      maxW={"400px"}
      display="flex"
      flexDirection="column"
      justifyContent="space-between"
    >
      <Flex justify="center" align="center" flex={1}>
        <Icon as={WeatherIcon} boxSize="150px" color="white" filter="drop-shadow(0 10px 10px rgba(0,0,0,0.1))" />
      </Flex>

      {data.timestamp_utc && (
        <Text
          position="absolute"
          top={4}
          right={6}
          fontSize="xs"
          color="gray.500"
          fontWeight="medium"
          bg="whiteAlpha.300"
          px={2}
          py={1}
          borderRadius="md"
        >
          Last Updated {new Date(data.timestamp_utc * 1000).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
        </Text>
      )}

      <Box>
        <Heading size="2xl" fontWeight="bold">
          {data.city}
        </Heading>
        <Text fontSize="6xl" fontWeight="bold" lineHeight="1">
          {Math.round(data.temperature_celsius)}°
        </Text>
        <Text fontSize="xl" fontWeight="medium" textTransform="capitalize">
          {data.description}
        </Text>
      </Box>
    </Box>
  );
};
