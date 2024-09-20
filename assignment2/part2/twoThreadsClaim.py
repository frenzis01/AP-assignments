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
         # This loop will be assigned as target to threads
         def seq_iter_loop (func,*args, **kwargs): 
            for _ in range (seq_iter):
               func(*args,**kwargs)
               
         # We must, for iter times, deploy n_threads threads, each running seq_iter_loop
         for _ in range(iter):
            workers = []   # needed to keep track of threads and join them later
            start = time.perf_counter()   # measure start time as thread deployment starts
            print("Deploying threads: " + str(start))
            for i in range(n_threads):
               t_args = (func,) + args
               workers.append(threading.Thread(target=seq_iter_loop,args=t_args,kwargs=kwargs))
               workers[i].start()

            for t in workers:
               t.join()
            

            end = time.perf_counter()
            exec_times.append(end - start)

         # mean and variance of execution times
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
         return execution_time
      return wrapper
   return decorator

def test(iter:int,fun:callable,args:tuple):

   def write_bench_to_file(n_threads:int,seq_iter:int,iter:int):
      res = bench(n_threads=n_threads, seq_iter=seq_iter, iter=iter)(fun)(*args)
      single_threaded_exec = basic_timing_decorator(iter=n_threads*seq_iter)(fun)(*args)
      res["single_thread_time"] = single_threaded_exec
      # TODO if fun.__name__ includes fib_run export it in folder fib_run
      outputFolder = "fib_run/" if "fib_run" in fun.__name__ else ""
      filename = f"out/{outputFolder}{fun.__name__}_{args}_{n_threads}_{seq_iter}.json"
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
   test(iter=5,fun=grezzo,args=(20,))
   test(iter=5,fun=fib_run,args=(15,))
   test(iter=5,fun=fib_run_recursive,args=(15,))

   # result = fib_run(28)
   # print(result)
   # result = rec_wrapper(fib_run_recursive,28)
   # print(result)

if __name__ == "__main__":
   main()
  
  
# "Two threads calling a function may take twice as much time as a single thread calling the function twice"
# From the `grezzo(10)` execution we can observe that the exec time increases along with n_threads, almost doubling -about 1.5x-,
# as expected
# However, if we execute grezzo(20), the exec time per thread is almost the same, probably due to the high workload (2**20 iterations);
# we can observe also considerably higher variance in the exec times.
# 
# 
# `just_wait` instead which represents I/O bound tasks, shows that the exec time per thread halves, as the number of threads doubles,
# since a thread waits part of the time when another is scheduled.
# 
# `fib_run` yields a similar result to `grezzo`, as it is a raw CPU workload, and the exec time increases with the number of threads.
# `fib_run_recursive` instead, shows a different behaviour, as the exec time per thread slightly increases, 
# but not as much as in the other cases.
# However, both of these two show slightly higher variance than `grezzo`.