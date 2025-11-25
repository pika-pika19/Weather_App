import { Input, Flex, IconButton } from "@chakra-ui/react";
import { LuSearch } from "react-icons/lu";
import { useState } from "react";

export const SearchBar = ({ onSearch }) => {
  const [city, setCity] = useState("");

  const handleSearch = () => {
    if (city.trim()) {
      onSearch(city);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleSearch();
    }
  };

  return (
    <Flex
      as="div"
      align="center"
      bg="white"
      _dark={{ bg: "gray.800" }}
      borderRadius="2xl"
      px={4}
      py={2}
      boxShadow="sm"
      w="full"
      maxW="400px"
      borderWidth="2px"
      borderColor="transparent"
      _focusWithin={{
        borderColor: "pink.500",
        _dark: {
          borderColor: "blue.500",
        },
      }}
      transition="all 0.2s"
    >
      <LuSearch color="gray" />
      <Input
        variant="none"
        bg="transparent"
        placeholder="Search city (e.g., Tokyo)"
        ml={2}
        value={city}
        onChange={(e) => setCity(e.target.value)}
        onKeyDown={handleKeyPress}
      />
    </Flex>
  );
};
