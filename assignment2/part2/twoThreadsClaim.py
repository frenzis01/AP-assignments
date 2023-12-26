import functools
import time
import threading
import statistics as stats
import json


def bench (n_threads:int=1,seq_iter:int=1,iter:int=1):
   exec_times = []
   def decorator(func):
      @functools.wraps(func)
      def wrapper(*args,**kwargs):
         def seq_iter_loop (func,*args, **kwargs): 
            for _ in range (seq_iter):
               func(*args,**kwargs)
         for _ in range(iter):
            workers = []
            start = time.perf_counter()
            print("Deploying threads: " + str(start))
            for i in range(n_threads):
               t_args = (func,) + args
               workers.append(threading.Thread(target=seq_iter_loop,args=t_args,kwargs=kwargs))
               workers[i].start()

            for t in workers:
               t.join()
            

            end = time.perf_counter()
            exec_times.append(end - start)

         mean = stats.mean(exec_times)
         variance = stats.variance(exec_times)

         return {
            'fun': func.__name__,
            'args': args,
            'n_threads': n_threads,
            'seq_iter': seq_iter,
            'iter': iter,
            'mean': mean,
            'variance': variance,
         }
      return wrapper
   return decorator
   
@bench(n_threads=2, seq_iter=8, iter=5)
def fib_run(n):
   a, b = 0, 1
   for _ in range(n):
      a, b = b, a + b
   return a

# When decorating this with bench, an infinite loop of thread creation starts 
def fib_run_recursive(n):
    if n <= 1:
        return n
    else:
        return fib_run_recursive(n-1) + fib_run_recursive(n-2)


@bench(n_threads=2, seq_iter=8, iter=5)
def rec_wrapper(func,*args,**kwargs):
   return func(*args,**kwargs)


def basic_timing_decorator(iter:int=1):
   def decorator(func):
      @functools.wraps(func)
      def wrapper(*args, **kwargs):
         start_time = time.perf_counter()
         for _ in range(iter):
            func(*args, **kwargs)
         end_time = time.perf_counter()
         execution_time = end_time - start_time
         # print(f"{func.__name__} took {execution_time:.6f} seconds to execute")
         return execution_time
      return wrapper
   return decorator

def test(iter:int,fun:callable,args:tuple):

   def write_bench_to_file(n_threads:int,seq_iter:int,iter:int):
      res = bench(n_threads=n_threads, seq_iter=seq_iter, iter=iter)(fun)(*args)
      single_threaded_exec = basic_timing_decorator(iter=n_threads*seq_iter)(fun)(*args)
      res["single_thread_time"] = single_threaded_exec
      filename = f"out/{fun.__name__}_{args}_{n_threads}_{seq_iter}.json"
      print(f"flushing to {filename}")
      with open(filename, 'w') as file:
         file.write(str(json.dumps(res, indent=3)))
      

   # TEST 1    <16=seq_iter,1=n_threads>
   write_bench_to_file(n_threads=1,seq_iter=16,iter=iter)
   # redundant n_threads=1

   # TEST 2    <8=seq_iter,2=n_threads>
   write_bench_to_file(n_threads=2,seq_iter=8,iter=iter)
   
   # TEST 3    <4=seq_iter,4=n_threads>
   write_bench_to_file(n_threads=4,seq_iter=4,iter=iter)
   
   # TEST 4    <2=seq_iter,8=n_threads>
   write_bench_to_file(n_threads=8,seq_iter=2,iter=iter)

def just_wait(n):
   # NOOP for n/10 seconds
   time.sleep(n * 0.1)

def grezzo(n):
   # CPU intensive
   for i in range(2**n):
      pass

def main():
   test(iter=5,fun=just_wait,args=(1,))
   test(iter=5,fun=grezzo,args=(10,))

   # result = fib_run(28)
   # print(result)
   # result = rec_wrapper(fib_run_recursive,28)
   # print(result)

if __name__ == "__main__":
   main()