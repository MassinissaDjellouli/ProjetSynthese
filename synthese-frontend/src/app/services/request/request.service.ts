import { Injectable } from '@angular/core';
import { ApiError } from '../../interfaces/ApiError';
import { ApiResponse } from 'src/app/interfaces/ApiResponse';
import { Errors } from '../../interfaces/ErrorsEnum';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  constructor() { }
  private apiLink = "http://localhost:8080/api/"

  getRequest = async (endpoint: string): Promise<ApiResponse | ApiError> => {
    const res = await fetch(this.apiLink + endpoint)
    if (res.ok) {
      return { data: await res.json() } as ApiResponse
    }
    return { error: await res.text(), status:res.status  } as ApiError
  }

  postRequest = async <T> (endpoint: string,body:T): Promise<ApiResponse | ApiError> => {
    const res = await fetch(this.apiLink + endpoint,{
      method:"POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body:JSON.stringify(body)
    })
    
    if (res.ok) {
      return { data: await res.json() } as ApiResponse
    }
    return await res.json() as ApiError
  }
}
export const isError = (res: ApiError | ApiResponse) => {
  try {
    return 'error' in res;
  } catch {
    return false;
  }
}
export const parseError = (res:ApiError | ApiResponse) => {
  let err = (res as ApiError).error
  let errors = Object.keys(Errors);
  console.log(err);
  
  if (err == undefined || !errors.includes(err)) {
    err = "UNKNOWN_ERROR"
  }
  return Errors[err as keyof typeof Errors];
}
