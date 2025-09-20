import React from "react";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { StoreContext } from "../../context/StoreContext";
import "./FoodItem.css";

const FoodItem = ({ name, description, id, imageURL, price }) => {
  const { increaseQty, decreaseQty, quantities } = useContext(StoreContext);
  console.log(quantities);
  return (
    <div className="col-12 col-sm-6 col-md-4 col-lg-2 mb-4 d-flex justify-content-center gap-4">
      <div className="card" style={{ maxWidth: "280px" }}>
        <Link to={`/food/${id}`}>
          <img
            src={imageURL}
            className="card-img-top"
            alt="Product Image"
            height={250}
          />
        </Link>

        <div className="card-body">
          <h5 className="card-title">{name}</h5>
          <p className="card-text" data-tooltip={description}>
            {description}
          </p>
          <div className="d-flex justify-content-between align-items-center">
            <span className="h5 mb-0">
              {new Intl.NumberFormat("vi-VN", {
                style: "currency",
                currency: "VND",
              }).format(price)}
            </span>
            <div>
              <i className="bi bi-star-fill text-warning"></i>
              <i className="bi bi-star-fill text-warning"></i>
              <i className="bi bi-star-fill text-warning"></i>
              <i className="bi bi-star-fill text-warning"></i>
              <i className="bi bi-star-half text-warning"></i>
              <small className="text-muted">(4.5)</small>
            </div>
          </div>
        </div>
        <div className="card-footer d-flex justify-content-between bg-light">
          <Link className="btn btn-primary btn-sm" to={`/food/${id}`}>
            View Food
          </Link>
          {quantities[id] > 0 ? (
            <div className="flex align-items-center gap-2">
              <button
                className="btn btn-danger btn-sm mr-2"
                onClick={() => decreaseQty(id)}
                style={{ marginRight: "10px" }}
              >
                <i className="bi bi-dash-circle"></i>
              </button>
              <span className="fw-bold">{quantities[id]}</span>
              <button
                className="btn btn-success btn-sm"
                style={{ marginLeft: "10px" }}
                onClick={() => increaseQty(id)}
              >
                <i className="bi bi-plus-circle"></i>
              </button>
            </div>
          ) : (
            <button
              className="btn btn-success btn-sm"
              onClick={() => increaseQty(id)}
            >
              <i className="bi bi-plus-circle"></i>
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
export default FoodItem;
