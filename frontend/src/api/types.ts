export interface SignUpRequestBody {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
}

export interface AuthenticationResponse {
    email: string;
    firstName: string;
    lastName: string;
    accessToken: string;
}
