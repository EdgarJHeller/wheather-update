import React, {useState, useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import axios from 'axios';

const StudentProfiles = () => {

  const [studentProfiles, setStudentProfiles] = useState([]);

  const fetchStudents = () => {
    axios.get("http://localhost:8080/api/v1/student").then(res =>{
      console.log(res);
      setStudentProfiles(res.data)
    });
  }

  useEffect(() => {
    fetchStudents();
  }, []);

  return studentProfiles.map((studentProfile, index) => {
    return (
      <div key={index}>
        <h1>{studentProfile.name}</h1>
        <ol>
          <p>{studentProfile.id}</p>
          <p>{studentProfile.email}</p>
          <p>{studentProfile.dob}</p>
          <p>{studentProfile.age}</p>
        </ol>
      </div>
    )
  });

}


function App() {
  return (
    <div className="App">
      <StudentProfiles />
    </div>
  );
}

export default App;
