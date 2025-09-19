import React, { useContext, useState } from "react";
import "./PlaceOrder.css";
import { assets, categories } from "../../assets/assets";
import AddressForm from "../../util/AddressForm";
import { StoreContext } from "../../context/StoreContext";
import { calculateCartTotals } from "../../util/cartUitl";
import { toast } from "react-toastify";
import axios from "axios";
import { API_BASE_URL } from "../../config/api";

const PlaceOrder = () => {
  const [userAddress, setUserAddress] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const { foodList, quantities, setQuantities, token } =
    useContext(StoreContext);
  // cart items
  const cartItems = foodList.filter((food) => quantities[food.id] > 0);
  console.log(cartItems.length);
  // calculation
  const { subtotal, shipping, tax, total } = calculateCartTotals(
    cartItems,
    quantities
  );
  const handleCheckout = async (e) => {
    e.preventDefault();

    try {
      // Tạo request body để gửi order lên backend
      const orderRequest = {
        orderItemList: cartItems.map((item) => ({
          foodId: item.id,
          name: item.name,
          price: item.price,
          quantity: quantities[item.id],
          category: item.category,
        })),
        userAddress: userAddress,
        amount: total, // tổng tiền
        email: email,
        phoneNumber: phoneNumber,
        orderStatus: "Preparing",
        fullName: `${firstName} ${lastName}`,
      };

      // Call API backend
      const res = await axios.post(
        `${API_BASE_URL}/api/vnpay/create-payment`,
        orderRequest,
        {
          headers: {
            Authorization: `Bearer ` + token,
          },
        }
      );

      // Backend trả về paymentUrl
      if (res.data) {
        window.location.href = res.data; // redirect sang VNPay
      }
    } catch (err) {
      console.error("Checkout error:", err);
      toast.error("Payment failed:", err);
    }
  };
  return (
    <div className="container mt-2">
      <main>
        <div className="py-5 text-center">
          <img
            className="d-block mx-auto"
            src={assets.logo}
            alt=""
            width="98"
            height="98"
          />
        </div>
        <div className="row g-5">
          {/* Cart */}
          <div className="col-md-5 col-lg-4 order-md-last">
            <h4 className="d-flex justify-content-between align-items-center mb-3">
              <span className="text-primary">Your cart</span>
              <span className="badge bg-primary rounded-pill">
                {cartItems.length}
              </span>
            </h4>

            <ul className="list-group mb-3">
              {cartItems.map((item) => (
                <li className="list-group-item d-flex justify-content-between lh-sm">
                  <div>
                    <h6 className="my-0">{item.name}</h6>
                    <small className="text-body-secondary">
                      Quantiies: {quantities[item.id]}
                    </small>
                  </div>
                  <span className="text-body-secondary">
                    {new Intl.NumberFormat("vi-VN", {
                      style: "currency",
                      currency: "VND",
                    }).format(item.price * quantities[item.id])}
                  </span>
                </li>
              ))}
              <li className="list-group-item d-flex justify-content-between">
                <div>
                  <h6>Shipping</h6>
                </div>
                <span className="text-body-secondary">
                  {subtotal == 0
                    ? 0.0
                    : new Intl.NumberFormat("vi-VN", {
                        style: "currency",
                        currency: "VND",
                      }).format(shipping)}
                </span>
              </li>

              <li className="list-group-item d-flex justify-content-between">
                <div>
                  <h6>Tax (10%)</h6>
                </div>
                <span className="text-body-secondary">
                  {new Intl.NumberFormat("vi-VN", {
                    style: "currency",
                    currency: "VND",
                  }).format(tax)}
                </span>
              </li>

              <li className="list-group-item d-flex justify-content-between">
                <h6>Total (VND)</h6>
                <strong>
                  {new Intl.NumberFormat("vi-VN", {
                    style: "currency",
                    currency: "VND",
                  }).format(total)}
                </strong>
              </li>
            </ul>
          </div>

          {/* Billing address */}
          <div className="col-md-7 col-lg-8">
            <h4 className="mb-3">Billing address</h4>
            <form
              className="needs-validation"
              noValidate
              onSubmit={handleCheckout}
            >
              <div className="row g-3">
                <div className="col-sm-6">
                  <label htmlFor="firstName" className="form-label">
                    First name
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="firstName"
                    placeholder="John"
                    required
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                  />
                </div>

                <div className="col-sm-6">
                  <label htmlFor="lastName" className="form-label">
                    Last name
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="lastName"
                    placeholder="Doe"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                  />
                </div>

                <div className="col-12">
                  <label htmlFor="email" className="form-label">
                    Email
                  </label>
                  <div className="input-group has-validation">
                    <span className="input-group-text">@</span>
                    <input
                      type="email"
                      className="form-control"
                      id="email"
                      placeholder="Email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div className="col-12">
                  <label htmlFor="address" className="form-label">
                    Phone Number
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="phone"
                    placeholder="9876543210"
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    required
                  />
                </div>

                <AddressForm onChange={setUserAddress} />
              </div>
              <hr className="my-4" />

              <button
                className="w-100 btn btn-primary btn-lg"
                type="submit"
                disabled={cartItems.length === 0}
              >
                Continue to checkout
              </button>
            </form>
          </div>
        </div>
      </main>
    </div>
  );
};
export default PlaceOrder;
