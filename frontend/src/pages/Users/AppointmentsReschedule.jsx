import React from "react";
import AdminLayout from "../../components/AdminLayout";
import { useForm } from "react-hook-form";
import { useSearchParams, useParams, useNavigate } from "react-router-dom";
import moment from "moment";
import axios from "axios";
import Toast from "react-hot-toast";
import {Navigate} from "react-router-dom"

export default function AppointmentsReschedule() {
  const { id } = useParams();
  let [searchParams, setSearchParams] = useSearchParams();
  if(localStorage.getItem("role")=="USER")
  return (
    <AdminLayout>
      <div className=" bg-white  shadow-sm mt-5 rounded-md">
        <BookingForm date={searchParams.get("date")} id={id} />
      </div>
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)

}

const BookingForm = ({ date, id }) => {
  const Navigate = useNavigate();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      date: moment(date).format("YYYY-MM-DD"),
      time: moment(date).format("HH:mm"),
    },
  });

  const onSubmit = async (data) => {
    if (!isFutureDate(data.date, data.time)) {
      return Toast.error("Date had expired");
    }
    try {
      await axios.put(
        `${import.meta.env.VITE_SERVER}/appointments/${id}/date?date=${new Date(
          `${data.date} ${data.time}`
        ).toISOString()}`,
        {},
        {
          headers: { Authorization: "Bearer " + localStorage.getItem("token") },
        }
      );
      Toast.success("Appointment updated");
      reset({ date: "", time: "" });
      Navigate("/user/appointment");
    } catch (err) {
      console.log(err);
    }
  };
  const generateTimeOptions = () => {
    const options = [];
    for (let hour = 10; hour <= 18; hour++) {
      for (let minute = 0; minute < 60; minute += 15) {
        const formattedHour = hour.toString().padStart(2, "0");
        const formattedMinute = minute.toString().padStart(2, "0");
        options.push(`${formattedHour}:${formattedMinute}`);
      }
    }
    return options;
  };

  const isFutureDate = (selectedDate, selectedTime) => {
    const selectedDateTime = new Date(`${selectedDate} ${selectedTime}`);
    const currentDateTime = new Date();
    return selectedDateTime > currentDateTime;
  };

  return (
    <form
      className="space-y-6 mx-auto max-w-5xl py-5"
      onSubmit={handleSubmit(onSubmit)}
    >
      <div>
        <label
          htmlFor="date"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Select Date
        </label>
        <div className="mt-2">
          <input
            id="date"
            name="date"
            type="date"
            {...register("date", {
              required: "Date is required",
            })}
            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
          />
          {errors.date && (
            <span className="text-red-500">{errors.date.message}</span>
          )}
        </div>
      </div>

      <div>
        <label
          htmlFor="time"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Select Time
        </label>
        <div className="mt-2">
          <select
            id="time"
            name="time"
            {...register("time", {
              required: "Time is required",
            })}
            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
          >
            <option value="">Select Time</option>
            {generateTimeOptions().map((time) => (
              <option key={time} value={time}>
                {time}
              </option>
            ))}
          </select>
          {errors.time && (
            <span className="text-red-500">{errors.time.message}</span>
          )}
        </div>
      </div>

      <button
        type="submit"
        className="flex w-fit justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        Book Now
      </button>
    </form>
  );
};
