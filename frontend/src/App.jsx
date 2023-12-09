import React, { useEffect, useRef, useState } from "react";
import AuthForm from "./components/AuthForm";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import RegisterUsers from "./pages/Admin/RegisterUsers";
import ViewUsers from "./pages/Admin/ViewUsers";
import EditUserInfo from "./pages/Admin/EditUserInfo";
import Profile from "./pages/Users/Profile";
import Appointments from "./pages/Users/Appointments";
import AdminAppointments from "./pages/Admin/Appointments";
import AppointmentsView from "./pages/Users/AppointmentsView";
import AppointmentsReschedule from "./pages/Users/AppointmentsReschedule";
import EditAppointments from "./pages/Admin/EditAppointment";
import Records from "./pages/Admin/Records";
import AllRecords from "./pages/Admin/AllRecords";
import EditRecords from "./pages/Admin/EditRecords";
import UsersRecords from "./pages/Users/UserRecords";
import ReportsDetails from "./pages/Users/ReportsDetails";
import Graph from "./pages/Users/Graph";
export default function App() {
  const [authorization, setAuthorization] = useState(null);
  const [role, setRole] = useState(null);
  useEffect(() => {
    const auth = localStorage.getItem('authorization');
    const userRole = localStorage.getItem('role');
    setAuthorization(auth);
    setRole(userRole);
    
  }, []);

  useEffect(()=>{
    console.log("instance")
    },[setRole])



  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<AuthForm />} />
          <Route path="/admin/register" element={<RegisterUsers />} />
        
                  <Route path="/admin/users" element={<ViewUsers />} />
                  <Route path="/admin/users/:email/edit" element={<EditUserInfo />} />
                  <Route path="/admin/appointments" element={<AdminAppointments />} />
                  <Route path="/admin/appointments/:id/edit" element={<EditAppointments />} />
                  <Route path="/admin/records" element={<Records />} />
                  <Route path="/admin/records/all" element={<AllRecords />} />
                  <Route path="/admin/records/:id/edit" element={<EditRecords />} />

               
                  <Route path="/user/appointment" element={<AppointmentsView />} />           
                  <Route  path="/user/profile" element={<Profile />} />
                  <Route path="/user/appointment/book" element={<Appointments />} />
                  <Route path="/user/appointments/:id/reschedule" element={<AppointmentsReschedule />} />
                  <Route path="/user/reports" element={<UsersRecords />} />
                  <Route path="/user/reports/:id" element={<ReportsDetails />} />
                  <Route path="/user/vitals" element={<Graph />} />

          
          <Route path="/*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
