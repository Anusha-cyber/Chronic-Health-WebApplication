import React, { useEffect, useState } from "react";
import AdminLayout from "../../components/AdminLayout";
import axios from "axios";
import { Link } from "react-router-dom";
import {Navigate} from "react-router-dom"

export default function UsersRecords() {
    if(localStorage.getItem("role")=="USER")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        User Records
      </h2>
      <div className=" bg-white  shadow-sm mt-5 rounded-md">
        <Records />
      </div>
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)

}

const Records = () => {
  const [Record, setRecords] = useState([]);
  useEffect(() => {
    getAllUsers();
  }, []);
  const getAllUsers = async () => {
    try {
      const users = await axios.get(
        `${
          import.meta.env.VITE_SERVER
        }/admin/health-records/user/${localStorage.getItem("email")}`,
        {
          headers: { Authorization: "Bearer " + localStorage.getItem("token") },
        }
      );
      setRecords(users.data);

      console.log(users.data);
    } catch (err) {
      console.log(err);
    }
  };
  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" className="px-6 py-3">
              Id
            </th>
            <th scope="col" className="px-6 py-3">
              Email
            </th>
            <th scope="col" className="px-6 py-3">
              Title
            </th>
            <th scope="col" className="px-6 py-3">
              Doctor Name
            </th>
            <th scope="col" className="px-6 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {Record.map((val) => (
            <tr className="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700">
              <th
                scope="row"
                className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
              >
                {val.id}{" "}
              </th>
              <td className="px-6 py-4">{val.email}</td>
              <td className="px-6 py-4">{val.title}</td>
              <td className="px-6 py-4">{val.doctorName}</td>
              <td className="px-6 py-4">
                <Link
                  to={`/user/reports/${val.id}?title=${val.title}&doctorName=${val.doctorName}&description=${val.description}&prescription=${val.prescription}&feedback=${val.feedback}&allergies=${val.allergies}&period=${val.period}&email=${val.email}&file=${val.recordsUrl}`}
                  className="font-medium text-blue-600  hover:underline"
                >
                  View
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
