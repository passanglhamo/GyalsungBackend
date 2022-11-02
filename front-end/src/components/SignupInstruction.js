import React from 'react';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
const SignupInstruction = () => {

  return (
    <div className='cards text-muted'>
      <div className='card-bodys p-4'>
        <div className='mb-2'>
          The National Service of Bhutan is a broad and profound concept which will have a
          transformative effect on the youth providing them with the opportunity to actualize
          their innate potential and unleash it in the service of Tsa-Wa-Sum.
        </div>
        <div className='mb-2'>
          The Constitution of Kingdom of Bhutan, Article 8 on the Fundamental Duties states
          that a Bhutanese Citizen shall render National Service when called upon to do so by
          the Parliament. The Gyalsung Bill reaffirms the National Service Obligation whereby
          all 18 years Bhutanese youth must undergo Gyalsung Training, provide National Service
          Duty and serve as National Service Reservist until the age of 45.
        </div>
        <div className='mb-2'>
          Considering the importance of the Gyalsung Program, vigorous sensitization and awareness
          programs will be carried out at all levels (schools, organizations, LG, etc) and through
        </div>
        <div className='mb-2'>
          <strong>You are required to fill up following information:</strong>
        </div>
        <div className='mb-1'>
          <CheckCircle style={{ color: '#11f64d' }} /> Correct CID number and DOB (Need to validate with the national census record)
        </div>
        <div className='mb-1'>
          <CheckCircle style={{ color: '#11f64d' }} /> Your correct mobile number (OTP verification will be sent to your mobile number)
        </div>
        <div className='mb-1'>
          <CheckCircle style={{ color: '#11f64d' }} /> Your valid email address (Verification code will be sent to your email inbox)
        </div>
      </div>
    </div>
  )
}

export default SignupInstruction;