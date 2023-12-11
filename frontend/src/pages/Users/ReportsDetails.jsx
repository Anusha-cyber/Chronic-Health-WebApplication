import React from 'react'
import AdminLayout from '../../components/AdminLayout'
import {Link, useParams,useSearchParams} from "react-router-dom"
import {Navigate} from "react-router-dom"

export default function ReportsDetails() {
  const {id} = useParams()
  const [searchParams,setSearchParams]=useSearchParams()
  if(localStorage.getItem("role")=="USER")
  return (
    <AdminLayout>
        <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        Report | {id}
      </h2>

      <div className='mt-5 bg-white p-5 shadow-sm rounded-md'>

<dl className="max-w-md text-gray-900 divide-y divide-gray-200 dark:text-white dark:divide-gray-700">
  <div className="flex flex-col pb-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Title</dt>
    <dd className="text-lg font-semibold">{searchParams.get("title")}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Description</dt>
    <dd className="text-lg font-semibold">{searchParams.get("description")}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">prescription</dt>
    <dd className="text-lg font-semibold">{searchParams.get("prescription")}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Doctor Name</dt>
    <dd className="text-lg font-semibold">{searchParams.get("doctorName")}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Allergy</dt>
    <dd className="text-lg font-semibold">{searchParams.get("allergies")!=="null"?searchParams.get("allergies"):""}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Period</dt>
    <dd className="text-lg font-semibold">{searchParams.get("period")!=="null"?searchParams.get("period"):""}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Feedback</dt>
    <dd className="text-lg font-semibold">{searchParams.get("feedback")!=="null"?searchParams.get("feedback"):""}</dd>
  </div>
  <div className="flex flex-col py-3">
    <dt className="mb-1 text-gray-500 md:text-lg dark:text-gray-400">Files</dt>
    <dd className="text-lg font-semibold">
      {searchParams.get("file")!=="null" && (
        <a target='_blank' download href={`${import.meta.env.VITE_SERVER}/files/download?filename=${searchParams.get("file")}`} >Download</a>
      )}
    
    </dd>
  </div>

</dl>

      </div>
    </AdminLayout>
  )
  else return(<Navigate replace to="/" />)

}
