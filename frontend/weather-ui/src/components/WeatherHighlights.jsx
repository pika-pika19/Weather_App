import { Box, Text, SimpleGrid, Flex, Icon } from "@chakra-ui/react";
import { FaTemperatureHigh, FaWind, FaTint } from "react-icons/fa";

const HighlightCard = ({ title, value, unit, icon, color }) => (
  <Box
    bg="white"
    _dark={{ bg: "gray.800" }}
    p={6}
    borderRadius="2xl"
    boxShadow="sm"
    display="flex"
    flexDirection="column"
    justifyContent="center"
    h="120px"
  >
    <Flex align="center" mb={2}>
      <Box
        p={2}
        borderRadius="lg"
        bg={`${color}.100`}
        _dark={{ bg: `${color}.900` }}
        color={`${color}.500`}
        mr={3}
      >
        <Icon as={icon} boxSize={5} />
      </Box>
      <Text fontSize="sm" color="gray.500" _dark={{ color: "gray.400" }} fontWeight="bold" letterSpacing="wide">
        {title}
      </Text>
    </Flex>
    <Text fontSize="3xl" fontWeight="bold" color="gray.800" _dark={{ color: "white" }}>
      {value}
      <Text as="span" fontSize="lg" ml={1} color="gray.500" _dark={{ color: "gray.400" }} fontWeight="normal">
        {unit}
      </Text>
    </Text>
  </Box>
);

export const WeatherHighlights = ({ data }) => {
  if (!data) return null;

  return (
    <SimpleGrid columns={1} gap={4} w="full">
      <HighlightCard
        title="FEELS LIKE"
        value={Math.round(data.feels_like_celsius)}
        unit="°C"
        icon={FaTemperatureHigh}
        color="orange"
      />
      <HighlightCard
        title="HUMIDITY"
        value={data.humidity}
        unit="%"
        icon={FaTint}
        color="blue"
      />
      <HighlightCard
        title="WIND SPEED"
        value={data.wind_speed}
        unit="km/h"
        icon={FaWind}
        color="teal"
      />
    </SimpleGrid>
  );
};
