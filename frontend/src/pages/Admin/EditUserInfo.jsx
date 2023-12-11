import React, { useEffect, useState } from "react";
import AdminLayout from "../../components/AdminLayout";
import { useParams } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import UserProfile from "../../components/UserProfile";
import {Navigate} from "react-router-dom"

export const getUserInfo = async (email) => {
  try {
    const user = await axios.get(
      `${import.meta.env.VITE_SERVER}/users/profile/${email}`,
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    );
    return user.data;
  } catch (err) {
    return err.response.data.error;
  }
};
export default function EditUserInfo() {
  const { email } = useParams();
  const [loading, setLoading] = useState(true);
  const [userInfo, setUserinfo] = useState();
  useEffect(() => {
    getInfo();
  }, [setUserinfo]);

  const getInfo = async () => {
    try {
      const resp = await getUserInfo(email);
      setUserinfo(resp);
    } catch (err) {
      toast.error(err);
    } finally {
      setLoading(false);
    }
  };
  if(localStorage.getItem("role")=="ADMIN")
  return (
    <AdminLayout>
      <h2 className="bg-white px-5 py-5 font-medium text-2xl text-primary rounded-md">
        Edit | {email}
      </h2>

      {!loading && <UserProfile user={userInfo} />}
    </AdminLayout>
  );
  else return(<Navigate replace to="/" />)
}
