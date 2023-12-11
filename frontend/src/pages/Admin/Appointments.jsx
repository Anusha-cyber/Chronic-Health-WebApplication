import React, { useEffect, useState } from "react";
import AdminLayout from "../../components/AdminLayout";
import axios from "axios";
import Toast from "react-hot-toast";
import moment from "moment";
import { Link } from "react-router-dom";
import {Navigate} from "react-router-dom"

export default function Appointments() {
    if(localStorage.getItem("role")=="ADMIN")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        User Appointments
      </h2>

      <AppointmentsList />
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)
}

const AppointmentsList = () => {
  const [appointmentList, setAppointmentsList] = useState([]);
  const getAppointments = async () => {
    try {
      const users = await axios.get(
        `${import.meta.env.VITE_SERVER}/appointments`,
        {
          headers: { Authorization: "Bearer " + localStorage.getItem("token") },
        }
      );
      setAppointmentsList(users.data);
      console.log(users.data);
    } catch (err) {
      Toast.error(err.response.data.error);
    }
  };
  useEffect(() => {
    getAppointments();
  }, []);
  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg mt-5">
      <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" className="px-6 py-3">
              ID
            </th>
            <th scope="col" className="px-6 py-3">
              User Email
            </th>
            <th scope="col" className="px-6 py-3">
              Name
            </th>
            <th scope="col" className="px-6 py-3">
              Date
            </th>
            <th scope="col" className="px-6 py-3">
              Status
            </th>
            <th scope="col" className="px-6 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {appointmentList.map((val) => {
            return (
              <tr className="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700">
                <th
                  scope="row"
                  className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                >
                  {val.id}
                </th>
                <td className="px-6 py-4">{val.userEmail}</td>
                <td className="px-6 py-4">{val.user.name}</td>
                <td className="px-6 py-4">
                  {console.log(
                    `${val.date[0]}-${val.date[1]}-${val.date[2]} ${val.date[3]} ${val.date[4]}`
                  )}
                  {moment(
                    `${val.date[0]}-${val.date[1]}-${val.date[2]} ${val.date[3]}:${val.date[4]}`
                  ).format("DD MMM YY HH:mm")}
                </td>
                <td className="px-6 py-4">{val.status}</td>
                <td className="px-6 py-4">
                  {val.status !== "CANCELED" && (
                    <Link
                      className="text-blue-500 cursor-pointer"
                      to={`/admin/appointments/${val.id}/edit?date=${moment(
                        `${val.date[0]}-${val.date[1]}-${val.date[2]} ${val.date[3]}:${val.date[4]}`
                      ).toISOString()}`}
                    >
                      {" "}
                      Edit
                    </Link>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};
