import React from "react";
import Navbar from "./Navbar";

export default function AdminLayout({ children }) {
  return (
    <div className="w-full bg-[#f1eef5] min-h-screen">
      <Navbar role={localStorage.getItem("role")} />
      <div className="max-w-screen-2xl mx-auto mt-5">{children}</div>
    </div>
  );
}
