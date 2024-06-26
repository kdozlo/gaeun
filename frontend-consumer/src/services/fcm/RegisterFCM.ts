import axios from "axios";
import Cookies from "universal-cookie";

const cookies = new Cookies()
const accessToken = cookies.get("accessToken")
const RegisterFCM = async (token: string) => {
    try {
        const response = await axios.post(import.meta.env.VITE_API_URL + "/api/fcmtokens", {
            "token": token,
        }, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        })
        console.log(response)
        return response
    }
    catch (err) {
        console.error(err);
        throw err
    }
};

export default RegisterFCM;