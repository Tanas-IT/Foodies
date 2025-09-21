import axios from "axios";
import { API_BASE_URL } from "../config/api";

export const addToCart = async (foodId, token) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/api/cart/add`,
      { foodId: foodId },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response;
  } catch (error) {
    console.log("Error while adding the cart data:", error);
    throw error;
  }
};

export const removeQuantityFromCart = async (foodId, token) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/api/cart/remove`,
      { foodId: foodId },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response;
  } catch (error) {
    console.log("Error while removing quantity from cart:", error);
    throw error;
  }
};
export const removeItemFromCart = async (foodId, token) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/api/cart/item/remove`,
      { foodId: foodId },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response;
  } catch (error) {
    console.log("Error while removing quantity from cart:", error);
    throw error;
  }
};

export const getCartData = async (token) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/cart`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response;
  } catch (error) {
    console.log("Error while fetching the cart data: ", error);
    // throw error;
  }
};
