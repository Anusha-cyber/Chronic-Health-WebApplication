import React, { useEffect, useState } from "react";
import AdminLayout from "../../components/AdminLayout";
import axios from "axios";
import Toast from "react-hot-toast";
import moment from "moment";
import { Link } from "react-router-dom";
import {Navigate} from "react-router-dom"

export default function AppointmentsView() {
    if(localStorage.getItem("role")=="USER")
  return (
    <AdminLayout>
      <div className=" bg-white  shadow-sm mt-5 rounded-md">
        <Appointments />
      </div>
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)

}

const Appointments = () => {
  const [appointment, setAppointments] = useState([]);
  const getUserAppointments = async () => {
    try {
      const appointments = await axios.get(
        `${import.meta.env.VITE_SERVER}/appointments/${localStorage.getItem(
          "email"
        )}`,
        {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );
      setAppointments(appointments.data);
      console.log(appointments.data);
    } catch (err) {
      Toast.error("Error getting appointments");
    }
  };
  useEffect(() => {
    getUserAppointments();
  }, []);
  const handleCancel = async (id) => {
    if (window.confirm("Are you sure you want to cancel")) {
      try {
        await axios.put(
          `${
            import.meta.env.VITE_SERVER
          }/appointments/${id}/status?newStatus=CANCELED`,
          {},
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        Toast.success("Appointments cancelled");
      } catch (err) {
        Toast.error(err.response.data.error);
      }
    }
  };
  return (
    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" class="px-6 py-3">
              ID
            </th>
            <th scope="col" class="px-6 py-3">
              Date
            </th>
            <th scope="col" class="px-6 py-3">
              Status
            </th>

            <th scope="col" class="px-6 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {appointment.map((val) => (
            <tr className="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700">
              <th
                scope="row"
                class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
              >
                {val.id}
              </th>
              <td className="px-6 py-4">
                {moment(
                  `${val.date[0]}-${val.date[1]}-${val.date[2]} ${val.date[3]}:${val.date[4]}`
                ).format("DD MMM YY HH:mm")}
              </td>
              <td class="px-6 py-4">{val.status}</td>

              <td class="px-6 py-4 space-x-3">
                {val.status !== "CANCELED" && (
                  <>
                    <Link
                      to={`/user/appointments/${
                        val.id
                      }/reschedule?date=${moment(
                        `${val.date[0]}-${val.date[1]}-${val.date[2]} ${val.date[3]}:${val.date[4]}`
                      ).toISOString()}`}
                      className="font-medium text-blue-600 dark:text-blue-500 hover:underline"
                    >
                      Reschedule
                    </Link>
                    <span
                      onClick={() => handleCancel(val.id)}
                      className="font-medium text-red-600 cursor-pointer hover:underline"
                    >
                      Cancel
                    </span>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
