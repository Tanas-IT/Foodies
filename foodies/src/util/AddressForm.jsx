import { useEffect, useState } from "react";

export default function AddressForm({ onChange }) {
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);

  const [selectedProvince, setSelectedProvince] = useState(null);
  const [selectedDistrict, setSelectedDistrict] = useState(null);
  const [selectedWard, setSelectedWard] = useState(null);
  const [street, setStreet] = useState("");

  // Lấy danh sách tỉnh
  useEffect(() => {
    fetch("https://provinces.open-api.vn/api/?depth=1")
      .then((res) => res.json())
      .then((data) => setProvinces(data));
  }, []);

  // Khi chọn tỉnh → load quận/huyện
  const handleProvinceChange = (e) => {
    const provinceCode = e.target.value;
    const province = provinces.find((p) => p.code === Number(provinceCode));
    setSelectedProvince(province);
    setSelectedDistrict(null);
    setSelectedWard(null);
    setWards([]);

    if (provinceCode) {
      fetch(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`)
        .then((res) => res.json())
        .then((data) => setDistricts(data.districts));
    }
  };

  // Khi chọn quận/huyện → load xã/phường
  const handleDistrictChange = (e) => {
    const districtCode = e.target.value;
    const district = districts.find((d) => d.code === Number(districtCode));
    setSelectedDistrict(district);
    setSelectedWard(null);

    if (districtCode) {
      fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`)
        .then((res) => res.json())
        .then((data) => setWards(data.wards));
    }
  };

  const handleWardChange = (e) => {
    const wardCode = e.target.value;
    const ward = wards.find((w) => w.code === Number(wardCode));
    setSelectedWard(ward);
  };

  // Gửi địa chỉ hoàn chỉnh ra ngoài
  useEffect(() => {
    if (onChange) {
      const fullAddress = [
        street,
        selectedWard?.name,
        selectedDistrict?.name,
        selectedProvince?.name,
      ]
        .filter(Boolean)
        .join(", ");
      onChange(fullAddress);
    }
  }, [street, selectedWard, selectedDistrict, selectedProvince, onChange]);

  return (
    <div className="row g-3">
      {/* Province */}
      <div className="col-md-4">
        <label htmlFor="province" className="form-label">
          Province/City
        </label>
        <select
          className="form-select"
          id="province"
          onChange={handleProvinceChange}
          required
        >
          <option value="">Chọn...</option>
          {provinces.map((p) => (
            <option key={p.code} value={p.code}>
              {p.name}
            </option>
          ))}
        </select>
      </div>

      {/* District */}
      <div className="col-md-4">
        <label htmlFor="district" className="form-label">
          District
        </label>
        <select
          className="form-select"
          id="district"
          onChange={handleDistrictChange}
          required
          disabled={!selectedProvince}
        >
          <option value="">Chọn...</option>
          {districts.map((d) => (
            <option key={d.code} value={d.code}>
              {d.name}
            </option>
          ))}
        </select>
      </div>

      {/* Ward */}
      <div className="col-md-4">
        <label htmlFor="ward" className="form-label">
          Ward
        </label>
        <select
          className="form-select"
          id="ward"
          onChange={handleWardChange}
          required
          disabled={!selectedDistrict}
        >
          <option value="">Chọn...</option>
          {wards.map((w) => (
            <option key={w.code} value={w.code}>
              {w.name}
            </option>
          ))}
        </select>
      </div>
      {/* Street */}
      <div className="col-12">
        <label htmlFor="street" className="form-label">
          Street / House No.
        </label>
        <input
          type="text"
          className="form-control"
          id="street"
          placeholder="1234 Main St"
          value={street}
          onChange={(e) => setStreet(e.target.value)}
          required
        />
      </div>
    </div>
  );
}
