import React, { useEffect } from "react";
import AdminLayout from "../../components/AdminLayout";
import RegisterUser from "../../components/Admin/RegisterUser";
import {Navigate} from "react-router-dom"
export default function RegisterUsers() {

  if(localStorage.getItem("role")=="ADMIN")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        Register Users
      </h2>
      <div className=" bg-white shadow-sm mt-5 rounded-md">
        <RegisterUser />
      </div>
    </AdminLayout>
  );

  else return(<Navigate replace to="/" />)
}
