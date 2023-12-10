import { HttpService } from "./HttpService";
import { HTTP_STATUS_CONFLICT, HTTP_STATUS_CREATED, HTTP_STATUS_NOT_FOUND, HTTP_STATUS_OK } from "./httpConstants";

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


export class AuthService {
    private httpService: HttpService;

    constructor(httpService: HttpService) {
        this.httpService = httpService;
    }

    public async logIn(email: string, password: string): Promise<AuthenticationResponse> {
        const response = await this.httpService.post("/api/auth/log-in", { email, password });
        if (response.status === HTTP_STATUS_NOT_FOUND) {
            throw new Error("mail/contraseña invalido/s");
        } else if (response.status !== HTTP_STATUS_OK) {
            throw new Error("Inicio de sesión falló inesperadamente");
        }
        
        const authResponse: AuthenticationResponse = await response.json();
        this.httpService.setAccessToken(authResponse.accessToken);
        return authResponse;
    }

    public async signUp(signUpData: SignUpRequestBody): Promise<AuthenticationResponse> {
        const response = await this.httpService.post("/api/auth/sign-up", signUpData);
        if (response.status === HTTP_STATUS_CONFLICT) {
            console.error(await response.text());
            throw new Error("El usuario ya existe");
        } else if (response.status !== HTTP_STATUS_CREATED) {
            console.error(await response.text());
            throw new Error("La creación del usuario falló inesperadamente");
        }

        const authResponse: AuthenticationResponse = await response.json();
        this.httpService.setAccessToken(authResponse.accessToken);
        return authResponse;
    }
}
