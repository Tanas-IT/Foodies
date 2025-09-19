const isDev = import.meta.env.VITE_API_DEVELOPMENT === "true";

export const API_BASE_URL = isDev
  ? import.meta.env.VITE_API_HOST
  : import.meta.env.VITE_API_DEPLOY;

export const API_GET_LOCATION = import.meta.env.VITE_API_GET_LOCATION;
export const WEB_ADMIN = import.meta.env.VITE_WEB_ADMIN;
