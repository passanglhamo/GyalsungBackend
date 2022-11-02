import React, { useState, useEffect } from "react";
import Button from '@material-ui/core/Button';
import Switch from '@material-ui/core/Switch';
import medicalbookingService from "../services/medicalbooking.service";

export const MedicalBooking = () => {
  const [allMedicalQuestion, setAllMedicalQuestion] = useState([]);
  const [questionDtos, setQuestionDtos] = useState([]);

  useEffect(() => {
    getAllMedicalQuestion();
  }, []);

  const getAllMedicalQuestion = async () => {
    medicalbookingService.getAllMedicalQuestion().then(
      response => {
        setAllMedicalQuestion(response.data);
        console.log(response.data)
      },
      error => {

      }
    );
  }

  const toggleSwitch = (value, index) => {
    if (allMedicalQuestion[index].isEnable === value) return;
    const question = allMedicalQuestion.map((item, idx) => {
      let { isEnable } = item;
      if (index === idx) {
        isEnable = value;
      }
      return { ...item, isEnable };
    });
    setAllMedicalQuestion(question);
  };

  const handleSubmit = (e) => {
    const medicalQuestionDtos = [];
    allMedicalQuestion.map((val, idx) => {
      let question = {
        medicalQuestionId: val.id,
        checkStatus: val.isEnable === true ? 'Y' : 'N',
      };
      medicalQuestionDtos.push(question);
    });
    // console.log(medicalQuestionDtos)

    let dzongkhagId = "2";

    const data = { dzongkhagId, medicalQuestionDtos };


    medicalbookingService.bookMedicalAppointment(data).then(
      response => {
        // setActiveStep((prevActiveStep) => prevActiveStep + 1);
      },
      error => {
        console.log(
          (error.response && error.response.data && error.response.data.message) ||
          error.message || error.toString()
        );
      }
    );
  };

  return (
    <>
      <div className='text-muted'>

        {allMedicalQuestion && allMedicalQuestion.map((item, idx) => {
          return (
            <>
              <span>{item.medicalQuestionName}</span>
              <Switch
                onChange={() => {
                  toggleSwitch(!item.isEnable, idx);
                }}
                // onChange={(e) => toggleSwitch(e, idx)}
                value={item.isEnabled}
                keyV={idx}
              />
            </>
          );
        })}

        <Button variant="contained" color="primary" onClick={handleSubmit} >
          Test Submit
        </Button>
      </div>
    </>
  );
}

export default MedicalBooking;