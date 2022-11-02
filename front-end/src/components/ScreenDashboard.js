import React from 'react'
import { Link, useParams, useNavigate } from 'react-router-dom';


const ScreenDashboard = () => {
    return (
        <div>
            <div className='col-md-12 row'>
                <div className='col-md-4'>
                    <div className='card'>
                        <div className='card-body'>
                            <h4 className='card-title'>Gyalsuup's screen</h4>
                            <div className='drop-divider'></div>
                            <p>1. <Link to="/signup"> Signup</Link></p>
                            <p>2. <Link to="/signin"> Signin</Link></p>
                            <p>3. <Link to="/gyalsupProfile"> Gyalsuup Profile</Link></p>
                            <p>4. <Link to="/enrolment"> Enrolment/Early Enlishment</Link></p>
                            <p>5. <Link to="/applyDeferment"> Apply for deferment</Link></p>
                            <p>6. <Link to="/defermentList"> Deferment List</Link></p>
                            <p>7. <Link to="/applyExemption"> Apply for exemption</Link></p>
                            <p>8. <Link to="/medicalBooking"> Book medical screening appointment</Link></p>
                            <p>9. <Link to="/testForm"> Sorting and academy assignment</Link></p>
                            <p>10. <Link to="/testForm"> Test Form</Link></p>
                        </div>
                    </div>
                </div>
                <div className='col-md-4'>
                    <div className='card'>
                        <div className='card-body'>
                            <h4 className='card-title'>Gyalsung admin screen</h4>
                            <div className='drop-divider'></div>
                            <p>1. <Link to="/signin"> User creation</Link></p>
                            <p>2. <Link to="/signin"> User access permission setup</Link></p>
                            <p>3. <Link to="/gyalsuupProfile"> Enlishment setup</Link></p>
                            <p>4. <Link to="/medicalQuestionanaire"> Course specialization setup</Link></p>
                            <p>5. <Link to="/medicalQuestionanaire"> Medical questionnaire setup</Link></p>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
}

export default ScreenDashboard