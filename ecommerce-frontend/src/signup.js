import React, { useState } from 'react';
import axios from 'axios';

function Signup() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [roles, setRoles] = useState(['user']); // Default role is user
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const signupRequest = {
      username: username,
      email: email,
      password: password,
      role: roles
    };

    try {
      const response = await axios.post('http://localhost:8080/api/auth/user/signup', signupRequest);
      // Assuming response.data is a string for success messages. Adjust as necessary.
    //   setMessage(response.data.message || 'User registered successfully!');
    
    if(response.status===200) alert("successfull");
    } catch (error) {
      if (error.response && error.response.data) {
        // Assuming error.response.data contains the error message. Adjust as necessary.
        setMessage(error.response.data.message || 'Signup failed. Please try again.');
      } else {
        setMessage('Signup failed. Please try again.');
      }
    }
  };

  const handleRoleChange = (e) => {
    const selectedRole = e.target.value;
    setRoles([selectedRole]); // If only one role is allowed, this is fine. Adjust if multiple roles are allowed.
  };

  return (
    <div>
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            minLength={3}
            maxLength={20}
          />
        </div>
        <div>
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            maxLength={50}
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={6}
            maxLength={40}
          />
        </div>
        <div>
          <label>Roles</label>
          <select onChange={handleRoleChange} value={roles[0] || 'user'}>
            <option value="user">User</option>
            <option value="admin">Admin</option>
            <option value="seller">Seller</option>
          </select>
        </div>
        <button type="submit">Sign Up</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}

export default Signup;
