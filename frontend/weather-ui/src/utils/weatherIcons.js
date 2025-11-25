import {
  WiDaySunny,
  WiCloud,
  WiRain,
  WiThunderstorm,
  WiSnow,
  WiFog,
  WiDayCloudy,
} from "react-icons/wi";

export const getWeatherIcon = (description) => {
  if (!description) return WiDayCloudy;
  const desc = description.toLowerCase();
  if (desc.includes("clear")) return WiDaySunny;
  if (desc.includes("few clouds")) return WiDayCloudy;
  if (desc.includes("clouds")) return WiCloud;
  if (desc.includes("rain") || desc.includes("drizzle")) return WiRain;
  if (desc.includes("thunder")) return WiThunderstorm;
  if (desc.includes("snow")) return WiSnow;
  if (desc.includes("mist") || desc.includes("fog")) return WiFog;
  return WiDayCloudy;
};
