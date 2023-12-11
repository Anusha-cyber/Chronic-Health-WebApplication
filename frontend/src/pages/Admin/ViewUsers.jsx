import React from "react";
import AdminLayout from "../../components/AdminLayout";
import UsersList from "../../components/Admin/UsersList";
import {Navigate} from "react-router-dom"

export default function ViewUsers() {
    if(localStorage.getItem("role")=="ADMIN")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        Users List
      </h2>
      <UsersList />
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)
}


