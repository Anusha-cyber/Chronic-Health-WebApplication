import React from "react";
import { useForm } from "react-hook-form";
import axios from "axios";
import toast from "react-hot-toast"
export default function RegisterUser() {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm();

  const onSubmit = async (data) => {
    try {
      await axios.post(`${import.meta.env.VITE_SERVER}/users/register`, {
        email: data.email,
        password: data.password,
        dateOfBirth: data.dob,
        role: "USER",
        address: data.address,
        name: data.name,
        phone: data.phone,
      },{headers:{'Authorization': `Bearer ${localStorage.getItem('token')}`}});
      toast.success("user registered")
      reset()
    } catch (err) {
        toast.error(err.response.data.error)
    }
  };
  return (
    <form className="mx-auto max-w-5xl py-5" onSubmit={handleSubmit(onSubmit)}>
      <div className="mb-5">
        <label
          htmlFor="email"
          className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          Email
        </label>
        <input
          type="email"
          id="email"
          {...register("email", {
            required: "Email is required",
            pattern: {
              value: /\S+@\S+\.\S+/,
              message: "Invalid email address",
            },
          })}
          className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-600 focus:border-indigo-600 block w-full p-2.5 ${
            errors.email ? "border-red-500" : ""
          }`}
          placeholder="name@gmail.com"
        />
        {errors.email && (
          <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
        )}
      </div>

      <div className="mb-5">
        <label
          htmlFor="password"
          className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          Password
        </label>
        <input
          type="password"
          id="password"
          {...register("password", {
            required: "Password is required",
            minLength: {
              value: 6,
              message: "Password must be at least 6 characters",
            },
          })}
          className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-600 focus:border-indigo-600 block w-full p-2.5 ${
            errors.password ? "border-red-500" : ""
          }`}
          placeholder="********"
        />
        {errors.password && (
          <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>
        )}
      </div>

      <div className="mb-5">
        <label
          htmlFor="dob"
          className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          Date of Birth
        </label>
        <input
          type="date"
          id="dob"
          {...register("dob", {
            required: "Date of Birth is required",
          })}
          className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-600 focus:border-indigo-600 block w-full p-2.5 ${
            errors.dob ? "border-red-500" : ""
          }`}
        />
        {errors.dob && (
          <p className="text-red-500 text-sm mt-1">{errors.dob.message}</p>
        )}
      </div>

      <div className="mb-5">
        <label
          htmlFor="address"
          className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          Address
        </label>
        <textarea
          id="address"
          {...register("address", {
            required: "Address is required",
          })}
          className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-600 focus:border-indigo-600 block w-full p-2.5 ${
            errors.address ? "border-red-500" : ""
          }`}
          placeholder="123 Main St, City, Country"
        />
        {errors.address && (
          <p className="text-red-500 text-sm mt-1">{errors.address.message}</p>
        )}
      </div>

      <div className="mb-5">
        <label
          htmlFor="phone"
          className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          Phone
        </label>
        <input
          type="tel"
          id="phone"
          {...register("phone", {
            required: "Phone is required",
          })}
          className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-600 focus:border-indigo-600 block w-full p-2.5 ${
            errors.phone ? "border-red-500" : ""
          }`}
          placeholder="123-456-7890"
        />
        {errors.phone && (
          <p className="text-red-500 text-sm mt-1">{errors.phone.message}</p>
        )}
      </div>

      <div className="mb-5">
        <label
          htmlFor="name"
          className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
        >
          Name
        </label>
        <input
          type="text"
          id="name"
          {...register("name", {
            required: "Name is required",
          })}
          className={`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-indigo-600 focus:border-indigo-800 block w-full p-2.5 ${
            errors.name ? "border-red-500" : ""
          }`}
          placeholder="John Doe"
        />
        {errors.name && (
          <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>
        )}
      </div>

      <button
        type="submit"
        className="bg-purple-800 text-white text-sm font-semibold px-4 py-2 rounded-lg hover:bg-purple-700 focus:outline-none focus:ring focus:border-purple-600"
      >
        Submit
      </button>
    </form>
  );
}
