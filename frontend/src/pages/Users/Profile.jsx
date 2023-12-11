import React, { useEffect, useState } from "react";
import AdminLayout from "../../components/AdminLayout";
import UserProfile from "../../components/UserProfile";
import { getUserInfo } from "../Admin/EditUserInfo";
import {Navigate} from "react-router-dom"

export default function Profile() {
  const [loading, setLoading] = useState(true);
  const [userInfo, setUserinfo] = useState();
  useEffect(() => {
    getInfo();
  }, [setUserinfo]);

  const getInfo = async () => {
    try {
      const resp = await getUserInfo(localStorage.getItem("email"));
      setUserinfo(resp);
    } catch (err) {
      toast.error(err);
    } finally {
      setLoading(false);
    }
  };
  if(localStorage.getItem("role")=="USER")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        Profile
      </h2>
      {!loading && (
        <div className=" bg-white shadow-sm mt-5 rounded-md">
          <UserProfile user={userInfo} />
        </div>
      )}
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)

}
