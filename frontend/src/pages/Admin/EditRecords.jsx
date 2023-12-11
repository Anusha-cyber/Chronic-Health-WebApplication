import React from "react";
import AdminLayout from "../../components/AdminLayout";
import { useParams, useSearchParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import Toast from "react-hot-toast";
import {Navigate} from "react-router-dom"

export default function EditRecords() {
  const { id } = useParams();
  if(localStorage.getItem("role")=="ADMIN")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        Record | {id}
      </h2>

      <div className="w-full bg-white py-5 mt-5 rounded-md shadow-sm">
        <MedicalForm id={id} />
      </div>
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)
}

const MedicalForm = ({ id }) => {
  let [searchParams, setSearchParams] = useSearchParams();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      title: searchParams.get("title"),
      prescription: searchParams.get("prescription"),
      description: searchParams.get("description"),
      doctorName: searchParams.get("doctorName"),
      allergies: searchParams.get("allergies"),
      feedback: searchParams.get("feedback"),
      period: searchParams.get("period"),
    },
  });

  const onSubmit = async (data) => {
    let body = {
      title: data.title,
      email: data.email,
      prescription: data.prescription,
      doctorName: data.doctorName,
      description: data.description,
      allergies: data.allergies,
      feedback: data.feedback,
      bp: data.bp,
      diabetic: data.diabetic,
      period: data.period,
    };
    if (data.file.length > 0) {
      body.recordsUrl = await handleFile(data.file[0]);
    }

    try {
      await axios.put(
        `${import.meta.env.VITE_SERVER}/admin/health-records/${id}`,
        body,
        {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );
      reset();
      Toast.success("Record Updated");
    } catch (err) {
      console.log(err);
    }
  };

  const handleFile = async (e) => {
    try {
      const formData = new FormData();
      formData.append("file", e);
      const res = await axios.post(
        `${import.meta.env.VITE_SERVER}/files/upload`,
        formData
      );
      return res.data;
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-6 bg-white max-w-5xl mx-auto"
    >
      <div className="mt-2">
        <label
          htmlFor="title"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Title
        </label>
        <input
          id="title"
          name="title"
          type="text"
          {...register("title", { required: "Title is required" })}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
        {errors.title && (
          <span className="text-red-500">{errors.title.message}</span>
        )}
      </div>
      <div className="mt-2">
        <label
          htmlFor="prescription"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Prescription
        </label>
        <textarea
          id="prescription"
          name="prescription"
          rows="4"
          {...register("prescription", {
            required: "Prescription is required",
          })}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
        {errors.prescription && (
          <span className="text-red-500">{errors.prescription.message}</span>
        )}
      </div>

      <div className="mt-2">
        <label
          htmlFor="description"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Description
        </label>
        <textarea
          id="description"
          name="description"
          rows="4"
          {...register("description", { required: "Description is required" })}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
        {errors.description && (
          <span className="text-red-500">{errors.description.message}</span>
        )}
      </div>
      <div className="mt-2">
        <label
          htmlFor="title"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          BP
        </label>
        <input
          id="bp"
          name="bp"
          type="text"
          {...register("bp", { required: "BP is required" })}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
        {errors.bp && <span className="text-red-500">{errors.bp.message}</span>}
      </div>

      <div className="mt-2">
        <label
          htmlFor="title"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Diabetic Record
        </label>
        <input
          id="diabetic"
          name="diabetic"
          type="text"
          {...register("diabetic", { required: "Diabetic is required" })}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
        {errors.diabetic && (
          <span className="text-red-500">{errors.diabetic.message}</span>
        )}
      </div>
      <div className="mt-2">
        <label
          htmlFor="doctorName"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Doctor's Name
        </label>
        <input
          id="doctorName"
          name="doctorName"
          type="text"
          {...register("doctorName", { required: "Doctor's Name is required" })}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
        {errors.doctorName && (
          <span className="text-red-500">{errors.doctorName.message}</span>
        )}
      </div>

      <div className="mt-2">
        <label
          htmlFor="allergies"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Allergies
        </label>
        <input
          id="allergies"
          name="allergies"
          type="text"
          {...register("allergies")}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
      </div>

      <div className="mt-2">
        <label
          htmlFor="period"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Period
        </label>
        <input
          id="period"
          name="period"
          type="text"
          {...register("period")}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
      </div>

      <div className="mt-2">
        <label
          htmlFor="feedback"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          Feedback
        </label>
        <textarea
          id="feedback"
          name="feedback"
          rows="4"
          {...register("feedback")}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
      </div>
      <div className="mt-2">
        <label
          htmlFor="feedback"
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          upload file
        </label>
        <input
          type="file"
          id="file"
          name="file"
          {...register("file")}
          className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        />
      </div>

      <button
        type="submit"
        className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        Submit
      </button>
    </form>
  );
};
