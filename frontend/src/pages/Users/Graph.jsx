import React, { useEffect, useState } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";
import faker from "faker";
import AdminLayout from "../../components/AdminLayout";
import axios from "axios";
import moment from "moment";
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export const options = {
  responsive: true,
  plugins: {
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "Vitals chart",
    },
  },
};

export const diabeticData = {
  datasets: [
    {
      label: "Diabetic data",
      borderColor: "rgb(255, 99, 132)",
      backgroundColor: "rgba(255, 99, 132, 0.5)",
    },
  ],
};

export const bpData = {
  datasets: [
    {
      label: "Dataset 1",
      borderColor: "rgb(255, 99, 132)",
      backgroundColor: "rgba(255, 99, 132, 0.5)",
    },
  ],
};

export default function Graph() {
  const [loading, setLoading] = useState(true);
  const [BPChartData,setBPChartData] = useState()
  const [DiabeticData,setDiabeticData] = useState()

  const getVitals = async () => {
    try {
      const rep = await axios.get(
        `${
          import.meta.env.VITE_SERVER
        }/admin/health-records/user/${localStorage.getItem("email")}/vitals`,
        {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );
      let bpData = [];
      let label = [];
      let diabetic = [];
      rep.data.map((val) => {
        if (val.bp != "") bpData.push(parseFloat(val.bp));
        if (val.timestamp != "") label.push(moment(val.timestamp).format("DD MMM YY HH:mm"));
        if (val.diabetic != "") diabetic.push(parseFloat(val.diabetic));
      });
     
      setBPChartData({
        labels:label,
        datasets: [
          {
            data:bpData,
            label: "BP vitals",
            borderColor: "rgb(255, 50, 132)",
            backgroundColor: "rgba(255, 99, 132, 0.5)",
          },
        ],
      })

      setDiabeticData({
        labels:label,
        datasets: [
          {
            data:diabetic,
            label: "Diabetic vitals",
            borderColor: "rgb(255, 99, 132)",
            backgroundColor: "rgba(255, 99, 132, 0.5)",
          },
        ],
      })


      setLoading(false);
    } catch (err) {
      console.log(err);
    }
  };
  useEffect(() => {
    getVitals();
  }, []);
  if (!loading)
    return (
      <AdminLayout>
        <div className="grid grid-cols-2 gap-8">
          <div className="bg-white rounded-md shadow-md">
        
            <Line options={options} data={DiabeticData} />
          </div>
          <div className="bg-white rounded-md shadow-md">
            <Line options={options} data={BPChartData} />
          </div>
        </div>
      </AdminLayout>
    );
}
