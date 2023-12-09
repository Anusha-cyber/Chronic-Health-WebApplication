import React from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import moment from "moment";
import Toast from "react-hot-toast"
const AdminLink = [
  {
    label: "User Registration",
    href: "/admin/register",
  },
  {
    label: "View Users",
    href: "/admin/users",
  },
  {
    label: "Appointments",
    href: "/admin/appointments",
  },
  {
    label: "Records",
    href: "/admin/records",
  },
];

const UserLink = [
  {
    label: "Appointments",
    href: "/user/appointment",
  },
  {
    label: "Book Appointment",
    href: "/user/appointment/book",
  },
  {
    label: "Reports",
    href: "/user/reports",
  },
  {
    label: "Vitals",
    href: "/user/vitals",
  },

  {
    label: "Profile",
    href: "/user/profile",
  },
];

export default function Navbar({ role }) {
  const navigate = useNavigate();
  const handleLogout = () => {
    setTimeout(() => {
      localStorage.clear();
    }, 500);

    navigate("/");
  };

  const handleEmergencyRequest =async()=>{
    try{
       
        await axios.post(`${import.meta.env.VITE_SERVER}/appointments?emergency=true`,{
            date: moment().add("day",1).toISOString()
        },{headers:{Authorization: `Bearer ${localStorage.getItem("token")}`}})
        Toast.success("Emergency Appoint requested for next available slot")
    }
    catch(err){
        Toast.error("Problem requesting appointments")

    }
  }

  return (
    <nav className="bg-white shadow-sm  border-gray-200 border ">
      <div className="max-w-screen-2xl   flex flex-wrap items-center justify-between mx-auto p-4">
        <Link
          to="/"
          className="flex items-center space-x-3 rtl:space-x-reverse"
        >
          <img
            className="mx-auto h-10 w-auto"
            src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
            alt=""
          />
        </Link>

        <div className="hidden w-full md:block md:w-auto" id="navbar-default">
          <ul className="font-medium flex flex-col p-4 md:p-0 mt-4 border border-gray-100 rounded-lg bg-gray-50 md:flex-row md:space-x-8 rtl:space-x-reverse md:mt-0 md:border-0 md:bg-white dark:bg-gray-800 md:dark:bg-gray-900 dark:border-gray-700">
            {role == "ADMIN" ? (
              <>
                {AdminLink.map((val) => {
                  return (
                    <li>
                      <Link
                        to={`${val.href}`}
                        className="block py-2 px-3 text-white bg-indigo-700 rounded md:bg-transparent md:text-indigo-700 md:p-0"
                      >
                        {val.label}
                      </Link>
                    </li>
                  );
                })}
              
                <li>
                  <span
                    onClick={() =>handleLogout()}
                    className="block py-2 px-3 text-white cursor-pointer bg-indigo-700 rounded md:bg-transparent md:text-indigo-700 md:p-0"
                  >
                    Logout
                  </span>
                </li>
              </>
            ) : (
              <>
                {UserLink.map((val) => {
                  return (
                    <li>
                      <Link
                        to={`${val.href}`}
                        className="block py-2 px-3 text-white bg-indigo-700 rounded md:bg-transparent md:text-indigo-700 md:p-0"
                      >
                        {val.label}
                      </Link>
                    </li>
                  );
                })}
                  <li>
                  <span
                    onClick={handleEmergencyRequest}
                    className="block py-2 px-3 text-white cursor-pointer bg-red-700 rounded md:bg-transparent md:text-red-700 md:p-0"
                  >
                    Emergency appointment
                  </span>
                </li>
                <li>
                  <span
                    onClick={handleLogout}
                    className="block py-2 px-3 text-white cursor-pointer bg-indigo-700 rounded md:bg-transparent md:text-indigo-700 md:p-0"
                  >
                    Logout
                  </span>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}
